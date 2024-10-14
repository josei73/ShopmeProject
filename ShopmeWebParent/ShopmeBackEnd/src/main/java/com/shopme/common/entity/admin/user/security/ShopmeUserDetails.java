package com.shopme.common.entity.admin.user.security;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ShopmeUserDetails implements UserDetails {

    private User user;

    public ShopmeUserDetails(User user) {
        this.user = user;
    }

    // Die Methode sammelt vom jeweiligen User die Role ein, die er hat und speichert sie in die List von SimpleGrand
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorise = new ArrayList<>();
        // S
        for (Role role : roles) {
            authorise.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorise;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }


    public String getFullName() {
        return this.user.getFullName();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public void setFirstName(String firstName) {
        this.user.setFirstName(firstName);
    }

    public void setLastname(String lastname) {
        this.user.setLastName(lastname);
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
        return user.isEnabled();
    }

    public boolean hasRole(String roleName){
        return user.hasRole(roleName);
    }
}
