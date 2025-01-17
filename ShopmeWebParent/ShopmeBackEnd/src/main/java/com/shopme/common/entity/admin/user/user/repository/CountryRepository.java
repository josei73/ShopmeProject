package com.shopme.common.entity.admin.user.user.repository;


import com.shopme.common.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country,Integer> {

    public List<Country> findAllByOrderByNameAsc();
}
