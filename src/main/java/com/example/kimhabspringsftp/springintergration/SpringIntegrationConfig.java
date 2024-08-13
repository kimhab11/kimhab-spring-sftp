package com.example.kimhabspringsftp.springintergration;

import com.example.kimhabspringsftp.SftpConfig;
import com.jcraft.jsch.ChannelSftp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.remote.gateway.AbstractRemoteFileOutboundGateway;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.dsl.Sftp;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;

@Configuration
public class SpringIntegrationConfig {
    @Resource
    private SftpConfig sftpConfig;

//    @Bean
//    public SessionFactory<ChannelSftp.LsEntry>  sftpSessionFactory() {
//        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
//        factory.setHost(sftpConfig.getHost());
//        factory.setPort(sftpConfig.getPort());
//        factory.setUser(sftpConfig.getUsername());
//        factory.setPassword(sftpConfig.getPassword());
//        factory.setAllowUnknownKeys(true);
//        return null;
//    }

//    @Bean
//    public MessageChannel sftpUploadChannel() {
//        return new DirectChannel();
//    }

//    @Bean
//    @ServiceActivator(inputChannel = "toSftpChannel")
//    public MessageHandler handler() {
//        FileTransferringMessageHandler<ChannelSftp.LsEntry> handler = new FileTransferringMessageHandler<>(sftpSessionFactory());
//        handler.setRemoteDirectoryExpressionString("'/remote/directory/'");
//        return handler;
//    }
}
