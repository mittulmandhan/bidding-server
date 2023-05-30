package com.liquorstore.user.server_impl;

import com.liquorstore.user.VO.Department;
import com.liquorstore.user.VO.ResponseTemplateVO;
import com.liquorstore.user.entity.CustomUser;
import com.liquorstore.user.repository.UserRepository;
import com.liquorstore.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public CustomUser saveUser(CustomUser user) {
        log.info("Inside saveUser of UserService");
        return userRepository.save(user);
    }

//    public ResponseTemplateVO getUserWithDepartment(Long userId) {
//        log.info("Inside getUserWithDepartment of UserServiceImpl");
//        ResponseTemplateVO vo = new ResponseTemplateVO();
//        CustomUser user = userRepository.findByUserId(userId);
//
//        Department department =
//                restTemplate.getForObject("http://DEPARTMENT-SERVICE/departments/" + user.getDepartmentId()
//                        , Department.class);
//        vo.setUser(user);
//        vo.setDepartment(department);
//
//        return vo;
//    }

}
