package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.admin.user.user.exception.BrandNotFoundExcpetion;
import com.shopme.common.entity.admin.user.user.exception.BrandNotFoundRestException;
import com.shopme.common.entity.admin.user.user.model.CategoryDTO;
import com.shopme.common.entity.admin.user.user.model.CategoryDTOMapper;
import com.shopme.common.entity.admin.user.user.service.BrandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class BrandRestController {

    @Autowired
    private BrandsService service;

    @Autowired
    private CategoryDTOMapper categoryDTOMapper;


     @PostMapping("/brands/check_unique")
    public String checkUnique(Integer id,String name){
        return service.checkUnique(id,name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId ) throws BrandNotFoundRestException {
         try {
             Brand brand = service.get(brandId);
             Set<Category> categories = brand.getCategories();
             return categories.stream().map(categoryDTOMapper).collect(Collectors.toList());
         }catch (BrandNotFoundExcpetion e){
             throw new BrandNotFoundRestException();
         }
    }

}
