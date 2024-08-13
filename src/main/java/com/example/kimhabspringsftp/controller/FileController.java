package com.example.kimhabspringsftp.controller;

import com.example.kimhabspringsftp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private FileService fileService;

//    @PostMapping("/upload/test")
//    public void uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            int size = 55;
//            MultipartFile[] multipartFiles = new MultipartFile[size];
//
//            for (int i = 0; i < size; i++) {
//                multipartFiles[i] = file;
//            }
//            fileService.uploadManyFileToSFTP(multipartFiles);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @PostMapping("/upload/single-file")
    public String uploadFiles(@RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadSingleFileToSFTP(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @PostMapping("/upload/many-file")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            fileService.uploadManyFileToSFTP(files);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @GetMapping("/save-local")
    public void saveFileToLocal(@RequestParam String filename) {
        fileService.saveFileFromSftpToLocal(filename);
    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) throws IOException {
        // Logic to fetch the file content (e.g., from a database or file system)
        // Replace the following line with your actual file retrieval logic
        InputStream fileStream = fileService.downloadFileFromSftp(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream));
    }
}
