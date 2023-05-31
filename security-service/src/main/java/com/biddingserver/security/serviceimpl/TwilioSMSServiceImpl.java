package com.biddingserver.security.serviceimpl;

import com.biddingserver.security.config.TwilioConfig;
import com.biddingserver.security.service.TwilioSMSService;
import com.biddingserver.security.utility.OtpStatus;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioSMSServiceImpl implements TwilioSMSService {

    @Autowired
    private TwilioConfig twilioConfig;


    public OtpStatus sendSMS(String messageBody, String to) {

        OtpStatus otpStatus = OtpStatus.FAILED;

        try {
            Message message = Message
                    .creator(new PhoneNumber(to)
                            ,new PhoneNumber(twilioConfig.getTrialNumber())
                            , messageBody)
                    .create();

            otpStatus = OtpStatus.DELIVERED;
            System.out.println("Message Sent!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return otpStatus;
    }

}
