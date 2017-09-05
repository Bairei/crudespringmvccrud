package com.bairei.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Bean
    public MailSender mailSender(Environment env) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mailserver.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("mailserver.port")));
        mailSender.setUsername(env.getProperty("mailserver.username"));
        mailSender.setPassword(env.getProperty("mailserver.password"));
        mailSender.setProtocol(env.getProperty("mail.transport.protocol"));

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        properties.put("mail.smtp.starttls.enable", env.getRequiredProperty("mail.smtp.starttls.enable"));
        properties.put("mail.smtp.socketFactory.class", env.getRequiredProperty("mail.smtp.socketFactory.class"));
        properties.put("mail.smtp.socketFactory.port", env.getRequiredProperty("mail.smtp.socketFactory.port"));

        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}