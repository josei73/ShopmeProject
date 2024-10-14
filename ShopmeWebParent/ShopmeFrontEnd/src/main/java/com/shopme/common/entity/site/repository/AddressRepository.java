package com.shopme.common.entity.site.repository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    public List<Address> findByCustomer(Customer customer);

    public Address findByIdAndCustomerId(Integer addressId, Integer customerId);

    public void deleteByIdAndCustomerId(Integer addressId, Integer customerId);


    @Query("update Address a SET a.defaultForShipping=true WHERE a.id=?1")
    @Modifying
    public void setDefaultAddress(Integer id);
    @Query("update Address a SET a.defaultForShipping= false" +
            " WHERE a.id != ?1 And a.customer.id = ?2 ")
    @Modifying
    public void setNonDefaultForOthers(Integer addressId,Integer customerId);

    @Query("select a from Address a where a.customer.id = ?1 AND a.defaultForShipping = true")
    public Address findDefaultCustomer(Integer customerID);




}
