package com.shopme.common.entity.site.security;

import com.shopme.common.entity.Customer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class CustomerUserDetails implements UserDetails {

    private Customer customer;


    public CustomerUserDetails(Customer customer) {
        this.customer = customer;
    }

    // Die Methode sammelt vom jeweiligen User die Role ein, die er hat und speichert sie in die List von SimpleGrand
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return null;
    }

    @Override
    public String getPassword() {
        return this.customer.getPassword();
    }


    public String getFullName() {
        return this.customer.getFullName();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    public void setFirstName(String firstName) {
        this.customer.setFirstName(firstName);
    }

    public void setLastname(String lastname) {
        this.customer.setLastName(lastname);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return customer.isEnabled();
    }

    public Customer getCustomer() {
        return customer;
    }


}
