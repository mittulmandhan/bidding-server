package com.liquorstore.security.repository;

import com.liquorstore.security.entity.OneTimePassword;
import com.liquorstore.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {
    OneTimePassword findByOtp(String userInputOtp);

    OneTimePassword findByUser(User byEmail);
}
