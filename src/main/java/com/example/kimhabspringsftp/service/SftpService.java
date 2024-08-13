package com.example.kimhabspringsftp.service;

import com.example.kimhabspringsftp.SftpConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Properties;

@Service
public class SftpService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Session session;
    private ChannelSftp channelSftp;

    @Resource
    private SftpConfig sftpConfig;

    public void connect() {
        try {
            JSch jSch = new JSch();
            session = jSch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort());
            session.setPassword(sftpConfig.getPassword());

            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            session.setConfig(properties);
            session.connect(60000);
            log.info("session server connected {} {}", session.getHost(), session.getPort());

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect(60000);
            log.info("channel Sftp connected");

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if ((channelSftp == null || !channelSftp.isConnected()) && (session == null || !session.isConnected())) {
                log.warn("Neither channelSftp nor session are connected");
            }

            if (channelSftp != null && channelSftp.isConnected()) {
                log.info("channel Sftp disconnect {}", channelSftp.pwd());
                channelSftp.disconnect(); // ending the active connection to the SFTP server.
                channelSftp.exit(); //  final cleanup and resource release after the disconnection.
            }

            if (session != null && session.isConnected()) {
                log.info("session disconnect {}", session.getHost());
                session.disconnect(); // ensures that the SSH session is properly terminated,
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public ChannelSftp getChannelSftp() {
        return channelSftp;
    }
}
