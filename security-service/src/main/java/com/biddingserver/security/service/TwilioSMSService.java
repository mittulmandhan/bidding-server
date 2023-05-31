package com.biddingserver.security.service;

import com.biddingserver.security.utility.OtpStatus;

public interface TwilioSMSService {

    public OtpStatus sendSMS(String messageBody, String to);

}
