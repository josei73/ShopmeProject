package com.shopme.common.entity.admin.user.user.controller;

import com.shopme.common.entity.admin.user.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(Integer id,String email) {
        return userService.isEmailUnique(id, email) ? "Ok" : "Duplicated";
    }
}
