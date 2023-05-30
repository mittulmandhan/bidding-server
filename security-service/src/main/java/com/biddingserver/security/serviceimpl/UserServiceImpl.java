package com.liquorstore.security.serviceimpl;

import com.liquorstore.security.entity.OneTimePassword;
import com.liquorstore.security.entity.PasswordResetToken;
import com.liquorstore.security.entity.User;
import com.liquorstore.security.entity.VerificationToken;
import com.liquorstore.security.model.ContactNumberModel;
import com.liquorstore.security.model.OTPModel;
import com.liquorstore.security.model.UserModel;
import com.liquorstore.security.repository.OneTimePasswordRepository;
import com.liquorstore.security.repository.PasswordResetTokenRepository;
import com.liquorstore.security.repository.UserRepository;
import com.liquorstore.security.repository.VerificationTokenRepository;
import com.liquorstore.security.service.UserService;
import com.liquorstore.security.utility.OtpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken
                 = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(token);
        if(verificationToken == null) {
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "valid";
    }

    @Override
    public VerificationToken getVerificationToken(String oldToken) {
        return verificationTokenRepository.findByToken(oldToken);
    }

    @Override
    public void deleteVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken
                = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null) {
            return "invalid token";
        }

        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();

        if((passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void savePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean isOldPasswordValid(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public User addContactNumber(ContactNumberModel contactNumber) {
        User user = userRepository.findByEmail(contactNumber.getUserName());
        user.setContactNumber(contactNumber.getNumber());
        userRepository.save(user);
        return user;
    }

    @Override
    public OtpStatus validateOTP(OTPModel otpModel) {
        User user = userRepository.findByEmail(otpModel.getUserName());
        OneTimePassword oneTimePassword = oneTimePasswordRepository.findByOtp(otpModel.getOtp());
        Calendar cal = Calendar.getInstance();

        if(oneTimePassword == null) {
            return OtpStatus.INVALID;
        }

        if((oneTimePassword.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            oneTimePasswordRepository.delete(oneTimePassword);
            return OtpStatus.EXPIRED;
        }

        user.setContactNumberVerified(true);
        oneTimePasswordRepository.delete(oneTimePassword);
        return OtpStatus.VALID;
    }

}
