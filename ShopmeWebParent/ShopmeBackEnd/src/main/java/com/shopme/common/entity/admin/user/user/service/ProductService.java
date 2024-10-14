package com.shopme.common.entity.admin.user.user.service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.user.repository.ProductRepository;
import com.shopme.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 5;
    @Autowired
    private ProductRepository repository;


    public List<Product> listAll() {
        return repository.findAll();
    }

    public void listByPage(int pageNumber, PagingAndSortingHelper helper, Integer categoryId) {
        Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNumber);
        String keyword = helper.getKeyword();
        Page<Product> page = null;

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
                page = repository.searchInCategory(categoryId, categoryIDMatch, keyword, pageable);
            } else
                page = repository.findAll(keyword, pageable);
        } else {
            if (categoryId != null && categoryId > 0) {
                String categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
                page = repository.findAllInCategory(categoryId, categoryIDMatch, pageable);
            } else
                page = repository.findAll(pageable);
        }


        helper.updateModelAttribute(pageNumber, page);
    }

    public void searchProduct(int pageNumber, PagingAndSortingHelper helper) {
        Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNumber);
        String keyword = helper.getKeyword();
        Page<Product> page = repository.searchProductByName(keyword, pageable);
        helper.updateModelAttribute(pageNumber, page);
    }


    public Product save(Product product) {
        if (product.getId() == null)
            product.setCreatedTime(new Date());
        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }
        product.setUpdatedTime(new Date());
        return repository.save(product);
    }

    public void saveProductPrice(Product productInForm) {
        Product productInDB = repository.findById(productInForm.getId()).get();
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setCost(productInForm.getCost());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());
        repository.save(productInDB);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = repository.findByName(name);

        if (isCreatingNew) {
            if (productByName != null)
                return "Duplicate";
        } else {
            if (productByName != null && productByName.getId() != id)
                return "Duplicate";
        }
        return "OK";
    }


    public void updateUserEnableStatus(Integer id, boolean enabled) {
        repository.updateEnableStatus(id, enabled);
    }


    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = repository.countById(id);
        if (countById == null | countById == 0)
            throw new ProductNotFoundException("Could not find any category with ID" + id);

        repository.deleteById(id);
    }


    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any category with ID" + id);
        }
    }
}
