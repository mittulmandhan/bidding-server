package com.biddingserver.user.service;

import com.biddingserver.user.VO.ResponseTemplateVO;
import com.biddingserver.user.entity.CustomUser;

public interface UserService {

    public CustomUser saveUser(CustomUser user);

//    public ResponseTemplateVO getUserWithDepartment(Long userId);
}
