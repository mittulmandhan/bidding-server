package com.biddingserver.security.service;

public interface MailSenderService {

    public boolean send(String to, String subject, String body);

}
