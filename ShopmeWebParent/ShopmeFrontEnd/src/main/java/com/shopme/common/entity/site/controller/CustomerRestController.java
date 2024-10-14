package com.shopme.common.entity.site.controller;

import com.shopme.common.entity.site.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customers/check_email")
    public String checkDuplicateEmail( String email) {
        return customerService.isEmailUnique(email) ? "Ok" : "Duplicated";
    }
}
