package com.example.kimhabspringsftp.controller;

import com.example.kimhabspringsftp.SftpConfig;
import com.example.kimhabspringsftp.service.SftpService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("sftp")
public class SftpController {
    @Autowired
    private SftpService sftpService;

    @Resource
    private SftpConfig sftpConfig;

//    @GetMapping("/test-other/connection")
//    public String testConnection(
//            @RequestParam String host,
//            @RequestParam int port,
//            @RequestParam String user,
//            @RequestParam String pass
//    ) {
//        try {
//            sftpService.connect(host, port, user, pass);
//            return "connected sftp " + host + " -p " + port;
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//    }

    @GetMapping("/connect")
    public String connect() {
        try {
            sftpService.connect();
            return "connected";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/disconnect")
    public String disconnect() {
        try {
            sftpService.disconnect();
            return "disconnected";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/channel")
    public String getChannel() throws SftpException {
        ChannelSftp channelSftp = sftpService.getChannelSftp();
        return channelSftp.toString();
    }
}
