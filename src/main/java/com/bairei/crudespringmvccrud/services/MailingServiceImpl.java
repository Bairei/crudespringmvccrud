package com.bairei.crudespringmvccrud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@PropertySource("classpath:mail.properties")
public class MailingServiceImpl implements  MailingService{

    private MailSender mailSender;
	private Environment env;

    @Autowired
    public MailingServiceImpl(MailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    private static final Logger log = Logger.getLogger(MailingServiceImpl.class.toString());

    @Override
    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(env.getProperty(("mailserver.username")));
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email to " + message.getTo()[0] + " has been sent.");
        } catch (MailException e) {
            log.severe("Unable to send an email message!\n" + e.toString());
        }

    }

}
