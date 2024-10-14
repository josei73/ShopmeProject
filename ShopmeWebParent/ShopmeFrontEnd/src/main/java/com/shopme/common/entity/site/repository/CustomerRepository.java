package com.shopme.common.entity.site.repository;

import com.shopme.common.entity.Customer;
import com.shopme.common.enums.AuthenticationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    public Customer findByEmail(String email);

    public Customer findByVerificationCode(String code);

    // Immer wenn Update Query genutzt wird, brauche ich Modifying
    @Query("Update Customer c set c.enabled = true,c.verificationCode=null  Where c.id = ?1")
    @Modifying
    public void enabled(Integer id);

    @Query("UPDATE Customer c set c.authenticationType= ?2 WHERE c.id=?1")
    @Modifying
    public void updateAuthenticationType(Integer customerId,AuthenticationType type);

    public Customer findByResetPasswordToken(String token);

}
