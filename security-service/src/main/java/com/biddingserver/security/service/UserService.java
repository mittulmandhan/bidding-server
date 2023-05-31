package com.biddingserver.security.service;

import com.biddingserver.security.entity.User;
import com.biddingserver.security.entity.VerificationToken;
import com.biddingserver.security.model.ContactNumberModel;
import com.biddingserver.security.model.OTPModel;
import com.biddingserver.security.model.UserModel;
import com.biddingserver.security.utility.OtpStatus;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken getVerificationToken(String oldToken);

    void deleteVerificationToken(VerificationToken verificationToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void savePassword(User user, String newPassword);

    boolean isOldPasswordValid(User user, String oldPassword);

    User addContactNumber(ContactNumberModel contactNumber);

    OtpStatus validateOTP(OTPModel otpModel);
}
