package com.liquorstore.security.serviceimpl;

import com.liquorstore.security.entity.OneTimePassword;
import com.liquorstore.security.repository.OneTimePasswordRepository;
import com.liquorstore.security.service.OneTimePasswordService;
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
