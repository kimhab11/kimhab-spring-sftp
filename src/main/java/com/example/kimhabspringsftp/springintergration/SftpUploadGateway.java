package com.example.kimhabspringsftp.springintergration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.io.File;
import java.util.List;

@MessagingGateway
public interface SftpUploadGateway {
    @Gateway(requestChannel = "sftpUploadChannel")
    void uploadFiles(List<File> files);
}
