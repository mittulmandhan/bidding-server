package com.liquorstore.security.service;

import com.liquorstore.security.utility.OtpStatus;

public interface TwilioSMSService {

    public OtpStatus sendSMS(String messageBody, String to);

}
