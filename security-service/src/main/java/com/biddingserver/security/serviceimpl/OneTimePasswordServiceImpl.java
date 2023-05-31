package com.biddingserver.security.serviceimpl;

import com.biddingserver.security.entity.OneTimePassword;
import com.biddingserver.security.repository.OneTimePasswordRepository;
import com.biddingserver.security.service.OneTimePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OneTimePasswordServiceImpl implements OneTimePasswordService {

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Override
    public void save(OneTimePassword oneTimePassword) {
        oneTimePasswordRepository.save(oneTimePassword);
    }

}
