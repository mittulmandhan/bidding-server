package com.liquorstore.security.controller;

import com.liquorstore.security.entity.User;
import com.liquorstore.security.entity.VerificationToken;
import com.liquorstore.security.event.ContactNumberAddEvent;
import com.liquorstore.security.event.RegistrationCompleteEvent;
import com.liquorstore.security.model.*;
import com.liquorstore.security.service.MailSenderService;
import com.liquorstore.security.service.UserService;
import com.liquorstore.security.serviceimpl.CustomAuthenticationProvider;
import com.liquorstore.security.serviceimpl.CustomUserDetailsService;
import com.liquorstore.security.utility.JWTUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/security")
public class RegistrationController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MailSenderService mailSenderService;

    @GetMapping("/hello")
    public String hello(Principal principal) {
        return "Hello! Welcome to my sample resource";
    }


    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully";
        }
        return "Bad User";
    }

    @PostMapping("/authenticate")
    public AuthenticationResponseDTO authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) throws Exception {
        try {
            customAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDTO.getUsername(),
                            authenticationRequestDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails
                = customUserDetailsService.loadUserByUsername(authenticationRequestDTO.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);

        return new AuthenticationResponseDTO(token);
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = userService.getVerificationToken(oldToken);
        User user = verificationToken.getUser();
        userService.deleteVerificationToken(verificationToken);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "Verification link sent";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);

        if(!result.equalsIgnoreCase("valid")) {
            return "invalid token";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);

        if(user.isPresent()) {
            userService.savePassword(user.get(), passwordModel.getNewPassword());
            return "Password Reset Successfully";
        } else {
            return "Invalid Token";
        }

    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if(!userService.isOldPasswordValid(user, passwordModel.getOldPassword()))
            return "Invalid Old Password";

        // save new password
        userService.savePassword(user, passwordModel.getNewPassword());
        return "Password changed successfully";
    }

    @PostMapping("/addContactNumber")
    public String addContactNumber(@RequestBody ContactNumberModel contactNumber) {
        User user = userService.addContactNumber(contactNumber);
        publisher.publishEvent(new ContactNumberAddEvent(user));
        return "Please check your phone for otp";
    }

    @PostMapping("/validateOTP")
    public String validateOTP(@RequestBody OTPModel otpModel) {

        return userService.validateOTP(otpModel).toString();

    }

//    @PostMapping("/getUserDetails")
//    public UserDetails getUserDetails(@RequestBody UserDetailsRequest userDetailsRequest) {
//        return customUserDetailsService.loadUserByUsername(userDetailsRequest.getUserName());
//    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;
        log.info("Click the link to reset your password: " + url);

        // send mail
        mailSenderService.send(user.getEmail(), "Reset Password", "Click the link to reset your password: " + url);

        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName()
                + ":" + request.getServerPort()
                + request.getContextPath();
    }
}
