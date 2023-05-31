package com.biddingserver.security.event.listener;

import com.biddingserver.security.entity.OneTimePassword;
import com.biddingserver.security.entity.User;
import com.biddingserver.security.event.ContactNumberAddEvent;
import com.biddingserver.security.service.OneTimePasswordService;
import com.biddingserver.security.service.TwilioSMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Random;

@Component
public class ContactNumberAddEventListener implements ApplicationListener<ContactNumberAddEvent> {

    @Autowired
    private OneTimePasswordService oneTimePasswordService;

    @Autowired
    private TwilioSMSService twilioSMSService;


    @Override
    public void onApplicationEvent(ContactNumberAddEvent event) {
        User user = event.getUser();
        String otp = generateOTP();
        OneTimePassword oneTimePassword = new OneTimePassword(user, otp);
        oneTimePasswordService.save(oneTimePassword);

        twilioSMSService.sendSMS("Hi " + user.getFirstName() + "! your otp is " + otp + ".\nThe otp will expire in " + oneTimePassword.getOTPValidity() + " minutes.\nDo not share your otp with anyone.", user.getContactNumber());
//        System.out.println("SendSMS method called");
    }

    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }

}
