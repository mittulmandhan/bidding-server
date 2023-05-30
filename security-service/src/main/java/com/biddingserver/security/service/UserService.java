package com.liquorstore.security.service;

import com.liquorstore.security.entity.User;
import com.liquorstore.security.entity.VerificationToken;
import com.liquorstore.security.model.ContactNumberModel;
import com.liquorstore.security.model.OTPModel;
import com.liquorstore.security.model.UserModel;
import com.liquorstore.security.utility.OtpStatus;

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
