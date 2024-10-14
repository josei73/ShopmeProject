package com.shopme.common.entity.site.repository;

import com.shopme.common.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
