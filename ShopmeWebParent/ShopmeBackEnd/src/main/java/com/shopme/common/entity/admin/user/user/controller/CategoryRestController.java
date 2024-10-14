package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.admin.user.user.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;

@RestController
public class CategoryRestController {

    @Autowired
    private CategoryService service;

    @PostMapping("/categories/check_unique")
    public String checkUnique(Integer id,  String name,
                               String alias){
        return service.checkUnique(id,name,alias);
    }

}
