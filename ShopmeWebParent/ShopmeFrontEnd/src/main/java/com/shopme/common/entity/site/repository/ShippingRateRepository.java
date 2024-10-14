package com.shopme.common.entity.site.repository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRateRepository extends JpaRepository<ShippingRate,Integer> {
    public ShippingRate findByCountryAndState(Country country, String state);
}
