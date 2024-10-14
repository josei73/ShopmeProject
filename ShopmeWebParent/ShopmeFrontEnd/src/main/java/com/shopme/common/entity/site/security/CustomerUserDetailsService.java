package com.shopme.common.entity.site.security;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepo.findByEmail(email);
        if (customer == null) throw new UsernameNotFoundException("Could not find customer with email: " + email);

        return new CustomerUserDetails(customer);

    }


}
