package com.shopme.common.entity.admin.user.user.repository;

import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.admin.user.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShippingRateRepository extends SearchRepository<ShippingRate,Integer> {

    public Long countById(Integer id);

    @Query("SELECT s FROM ShippingRate s WHERE CONCAT(s.id,' ',s.country.name,' ',s.state) LIKE %?1% ")
    public Page<ShippingRate> findAll(String keyword, Pageable pageable);


    public ShippingRate findByCountryIdAndState(Integer countryId,String state);


    @Query("UPDATE ShippingRate s set s.codSupported= ?2 where s.id=?1")
    @Modifying
    public void updateCodSupport (Integer id,boolean enabled);

}
