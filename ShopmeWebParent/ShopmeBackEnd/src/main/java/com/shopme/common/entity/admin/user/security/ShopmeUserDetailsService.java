package com.shopme.common.entity.admin.user.security;

import com.shopme.common.entity.User;
import com.shopme.common.entity.admin.user.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class ShopmeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if (user != null) return new ShopmeUserDetails(user);

        throw new UsernameNotFoundException("Could not find user with email: " + email);
    }


}
