package com.vti.testing.email;

import com.vti.testing.filter.TokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {
    private final Logger log = LoggerFactory.getLogger(TokenFilter.class);
    @Autowired
    private JavaMailSender mailSender;
    public static final String FROM_EMAIL = "kyl.kt95@gmail.com";
    public void sendSimpleEmail(String toEmail, String body, String subject){
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(FROM_EMAIL);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

//        mailSender.send(message);
        System.out.println("Sending mail...");

    }
    @Async
    public void sendEmailWithAttachment(String toEmail, String body, String subject, String attachment) throws MessagingException {
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true);

        mimeMessageHelper.setFrom(FROM_EMAIL);
        mimeMessageHelper.setTo(new String[]{toEmail, "kyl.kt95@gmail.com"});
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        FileSystemResource fileSystem = new FileSystemResource(new File(attachment));
        mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
        mailSender.send(mimeMailMessage);
        log.info("mime mail sending...");
    }
}
