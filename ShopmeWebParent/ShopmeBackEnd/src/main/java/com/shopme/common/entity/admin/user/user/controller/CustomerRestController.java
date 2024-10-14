package com.shopme.common.entity.admin.user.user.controller;

import com.shopme.common.entity.admin.user.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customers/check_email")
    public String checkDuplicateEmail(Integer id,String email) {
        return customerService.isEmailUnique(id,email) ? "Ok" : "Duplicated";
    }
}
