package com.shopme.common.entity.admin.user.user.service;


import com.shopme.common.entity.Brand;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.user.exception.BrandNotFoundExcpetion;
import com.shopme.common.entity.admin.user.user.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandsService {
    public static final int BRANDS_Per_Page = 10;

    @Autowired
    private BrandRepository repository;

    public List<Brand> listAll() {
        return repository.findAll();
    }

    public Brand get(Integer id) throws BrandNotFoundExcpetion {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundExcpetion("Could not find any brand with ID " + id);
        }
    }

    public void listByPage(int pageNumber, PagingAndSortingHelper helper) {
        helper.listEntities(pageNumber,BRANDS_Per_Page,repository);

    }


    public Brand save(Brand brand) {
        return repository.save(brand);
    }

    public void delete(Integer id) throws BrandNotFoundExcpetion {
        Long countById = repository.countById(id);
        if (countById == null | countById == 0)
            throw new BrandNotFoundExcpetion("Could not find any brand with ID" + id);

        repository.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Brand brandByName = repository.findByName(name);

        if (isCreatingNew) {
            if (brandByName != null)
                return "Duplicate";
        } else {
            if (brandByName != null && brandByName.getId() != id)
                return "Duplicate";
        }
        return "OK";
    }
}
