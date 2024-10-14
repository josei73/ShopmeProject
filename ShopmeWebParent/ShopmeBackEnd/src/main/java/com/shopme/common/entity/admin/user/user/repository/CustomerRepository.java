package com.shopme.common.entity.admin.user.user.repository;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.admin.user.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface CustomerRepository extends SearchRepository<Customer, Integer> {

    public Customer findByEmail(String email);

    @Query("Update Customer c set c.enabled = ?2 Where c.id = ?1")
    @Modifying
    public void updateEnableStatus(Integer id, boolean enabled);

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email,' ',c.firstName,' ', c.lastName,' '" +
            ",c.city,' ',c.state,' ',c.country.name,' '," +
            "c.addressLine1,' ',c.addressLine2,' ',c.postalCode) LIKE %?1% ")
    public Page<Customer> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);

}
