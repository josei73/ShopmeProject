package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.admin.user.user.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CountryRestController {
    @Autowired private CountryRepository repository;

    @GetMapping("/countries/list")
    public List<Country> listAll(){
        return repository.findAllByOrderByNameAsc();
    }

    @PostMapping("/countries/save")
    public String save(@RequestBody Country country){
        Country savedCountry = repository.save(country);
        return String.valueOf(savedCountry.getId());
    }

    @DeleteMapping("countries/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        repository.deleteById(id);
    }
}
