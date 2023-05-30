package com.liquorstore.security.serviceimpl;

import com.liquorstore.security.service.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean send(String to, String subject, String body) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom("mittul.store.liquor@gmail.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);

        mailSender.send(mail);
        System.out.println("Mail Sent!");
        return true;
    }
}
