package com.shopme.common.entity.admin.user.user.repository;

import com.shopme.common.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Integer> {
    public List<Currency> findAllByOrderByNameAsc();
}
