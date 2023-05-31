package com.biddingserver.security.service;

import com.biddingserver.security.entity.OneTimePassword;

public interface OneTimePasswordService {
    void save(OneTimePassword oneTimePassword);
}
