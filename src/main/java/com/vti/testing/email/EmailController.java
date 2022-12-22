package com.vti.testing.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/send-email")
@RequiredArgsConstructor
public class EmailController {
//    @Autowired
    final EmailProperties emailProperties;
//    @Autowired
    final MailService mailService;

//    public void triggerMail(){
//        mailService.sendSimpleEmail(emailProperties.getUserName(),"Welcom you to NccAsia Company","Hello Khánh đẹp zai!");
//    }
    @GetMapping
//    @Scheduled(cron="0 0 0 3 * ?")
    public void triggerMimeMail() throws MessagingException {
        long startTime = System.currentTimeMillis();

        mailService.sendEmailWithAttachment(emailProperties.getUserName(),
                "Welcom you to NccAsia Company","Hello Khánh đẹp zai","C:\\Khánh\\CV\\46664434_1053506044810421_3445620711303938048_n.jpg");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Time to send mail  " + duration);
    }
}
