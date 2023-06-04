package com.biddingserver.security.service;

public interface MailSenderService {

    public void send(String to, String subject, String body);

}
