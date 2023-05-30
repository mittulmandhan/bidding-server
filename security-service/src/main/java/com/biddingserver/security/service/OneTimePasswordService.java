package com.liquorstore.security.service;

import com.liquorstore.security.entity.OneTimePassword;

public interface OneTimePasswordService {
    void save(OneTimePassword oneTimePassword);
}
