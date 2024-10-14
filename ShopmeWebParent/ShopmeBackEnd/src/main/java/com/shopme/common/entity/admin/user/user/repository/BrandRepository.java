package com.shopme.common.entity.admin.user.user.repository;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;
import com.shopme.common.entity.admin.user.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends SearchRepository<Brand,Integer> {

    public Brand findByName(String name);

    public Long countById(Integer id);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1% ")
    public Page<Brand> findAll(String keyword, Pageable pageable);

    @Query("SELECT New Brand(b.id,b.name) FROM Brand b ORDER BY b.name ASC ")
    public List<Brand> findAll();




}
