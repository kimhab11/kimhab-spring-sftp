package com.example.kimhabspringsftp.service;

import com.example.kimhabspringsftp.SftpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class FileService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private SftpService sftpService;

    private final String UPLOAD_PATH = "/upload";
    private final String DOWNLOAD_PATH = "/download";
    @Resource
    private SftpConfig sftpConfig;

    public void uploadSingleFileToSFTP(MultipartFile multipartFile){
        sftpService.connect();
        try {
            writeFileSftp(multipartFile, multipartFile.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpService.disconnect();
        }
    }

    public void uploadManyFileToSFTP(MultipartFile[] files) {
        sftpService.connect();
        try {
            uploadFilesBatch(files);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpService.disconnect();
        }
    }

    public void uploadFilesBatch(MultipartFile[] files) throws Exception {
        int batchSize = 5; // Set your batch size
        int batchNo = 0;
        log.info("Uploading batch file count: {}, file size per batch: {}", files.length, batchSize);

        for (int i = 0; i < files.length; i += batchSize) {
            batchNo++;
            log.info("batch no {}", batchNo);

            int end = Math.min(i + batchSize, files.length);
            MultipartFile[] batch = Arrays.copyOfRange(files, i, end); // batch.length = batchSize
            uploadBatch(batch, batchNo);

            log.info("batch no: {}, total upload: {}, remain: {}", batchNo, end, files.length-end);
        }
        log.info("Completed batch upload");
    }

    private void uploadBatch(MultipartFile[] batch, int batchNo) throws Exception {
        int i = 0;
        for (MultipartFile file : batch) {
            String newName = batchNo+"_"+i++ +"_"+file.getOriginalFilename();
            writeFileSftp(file, newName);
        }
    }

    private void writeFileSftp(MultipartFile file, String filename) {
        boolean success = false;
        int attempts = 0;
       //   String filename = file.getOriginalFilename();
        log.info("filename: [{}]", filename);
        while (!success && attempts < 3) {
            try {

                sftpService.getChannelSftp().put(file.getInputStream(), UPLOAD_PATH + "/" + filename);
                log.info("Upload: [{}] Successfully, into {}", filename, UPLOAD_PATH);
                success = true;
            } catch (Exception e) {
                log.error("Attempt " + (attempts + 1) + ": " + e.getMessage());
                attempts++;
                e.printStackTrace();
                if (attempts >= 2) {
                    log.error("After 2 failed attempts" + e.getMessage());
                }
            }
        }
    }

    public void saveFileFromSftpToLocal(String remoteFileName) {
        sftpService.connect();
        String pathRemoteDownload = "/download/";
        String localSaveDir = "src/main/resources/static/";
        boolean success = false;
        int attempts = 0;
        log.info("remoteFileName: [{}]", remoteFileName);
        while (!success && attempts < 3) {
            try {
                log.info("file path: {}", pathRemoteDownload+remoteFileName);
                sftpService.getChannelSftp().get(pathRemoteDownload+remoteFileName,  localSaveDir+remoteFileName);
                log.info("saved to local: {}", localSaveDir);
                success = true;
            } catch (Exception e) {
                log.error("Attempt " + (attempts + 1) + ": " + e.getMessage());
                attempts++;
                e.printStackTrace();
                if (attempts >= 2) {
                    log.error("After 2 failed attempts" + e.getMessage());
                }
            }
        }
        sftpService.disconnect();
    }

    public InputStream downloadFileFromSftp(String remoteFileName) {
        sftpService.connect();
        boolean success = false;
        int attempts = 0;
        log.info("Remote File Name: [{}]", remoteFileName);
        while (!success && attempts < 3) {
            try {
                String remoteFileLocation = DOWNLOAD_PATH+"/"+remoteFileName;
                log.info("remote file path: {}", remoteFileLocation);
                success = true;
                return sftpService.getChannelSftp().get(remoteFileLocation);
            } catch (Exception e) {
                log.error("Attempt " + (attempts + 1) + ": " + e.getMessage());
                attempts++;
                e.printStackTrace();
                if (attempts >= 2) {
                    log.error("After 2 failed attempts" + e.getMessage());
                }
            }
        }
        sftpService.disconnect();
        return null;
    }
}
