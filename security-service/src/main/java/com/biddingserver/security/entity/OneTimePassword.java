package com.liquorstore.security.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class OneTimePassword {

    private static final int EXPIRATION_TIME = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String otp;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_VERIFY_OTP"))
    private User user;

    public OneTimePassword(User user, String otp) {
        super();
        this.otp = otp;
        expirationTime = calculateExpirationDate(EXPIRATION_TIME);
        this.user = user;
    }

    private Date calculateExpirationDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }

    public int getOTPValidity() {
        return EXPIRATION_TIME;
    }
}
