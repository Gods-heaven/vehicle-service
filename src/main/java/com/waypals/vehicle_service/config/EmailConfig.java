package com.waypals.vehicle_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${email.smtp.host}")
    private String host;
    @Value("${email.smtp.username}")
    private String username;
    @Value("${email.smtp.password}")
    private String password;
    @Value("${email.smtp.port}")
    private Integer port;
    @Value("${email.mailType}")
    private String mailType;
    @Value("${email.charset}")
    private String charset;
    @Value("${email.wordwrap}")
    private String wordwrap;
    @Bean
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mailtype", mailType);
        properties.setProperty("charset",charset);
        properties.setProperty("wordwrap",wordwrap);
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }
}

