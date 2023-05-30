package com.liquorstore.user.service;

import com.liquorstore.user.VO.ResponseTemplateVO;
import com.liquorstore.user.entity.CustomUser;

public interface UserService {

    public CustomUser saveUser(CustomUser user);

//    public ResponseTemplateVO getUserWithDepartment(Long userId);
}
