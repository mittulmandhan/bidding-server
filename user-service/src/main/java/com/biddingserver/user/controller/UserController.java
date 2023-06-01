package com.biddingserver.user.controller;

import com.biddingserver.user.VO.ResponseTemplateVO;
import com.biddingserver.user.entity.CustomUser;
import com.biddingserver.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public CustomUser saveUser(@RequestBody CustomUser user) {
        log.info("Inside saveUser of UserController");
        return userService.saveUser(user);
    }

//    @GetMapping("/{id}")
//    public ResponseTemplateVO getUserWithDepartment(@PathVariable("id") Long userId) {
//        log.info("Inside getUserWithDepartment of UserController");
//        return userService.getUserWithDepartment(userId);
//    }

}
