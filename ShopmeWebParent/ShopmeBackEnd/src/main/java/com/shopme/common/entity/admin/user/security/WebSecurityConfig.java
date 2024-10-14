package com.shopme.common.entity.admin.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        return new ShopmeUserDetailsService();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Das sagt, dass die Authentifizierung von den Daten in Datenbank ausgeht deswegen dao
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/states/list_by_country/**").hasAnyAuthority("Admin","Salesperson")
                .antMatchers("/users/**","/settings/**","/countries/**","/states/**").hasAuthority("Admin")



                .antMatchers("/products/new","/products/delete/**").hasAnyAuthority("Admin","Editor")

                .antMatchers("/products/edit/**","/products/save","/products/check_unique").hasAnyAuthority("Admin","Editor","Salesperson")

                .antMatchers("/products","/products/", "/products/detail/**","/products/page/**")
                .hasAnyAuthority("Admin","Editor","Salesperson","Shipper")

                .antMatchers("/products/**").hasAnyAuthority("Admin","Editor")
                .antMatchers("/orders","/orders/","/orders/page/**","/orders/detail/**").hasAnyAuthority("Admin","Salesperson","Shipper")
                .antMatchers("/customers/**","/shipping/**","/reports/**","/get_shipping_cost","/orders/**","/reports/**")
                .hasAnyAuthority("Admin","Salesperson")
                .antMatchers("/orders_shipper/update/**").hasAuthority("Shipper")

                .antMatchers("/brands/**","/categories/**","/articles/**","/menus/**")
                .hasAnyAuthority("Admin","Editor")
                .antMatchers("/questions/**","/reviews/**").hasAnyAuthority("Admin","Assistant")
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login").usernameParameter("email").permitAll().and().logout().permitAll()
                .and().rememberMe().key("AbcDefgHijKlmnOpqrs_1234567890").tokenValiditySeconds(7* 24*60 * 60);
        http.headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //Damit ich die daten aus dem Ordner nutzen kann ignoriert er die authentication für diese Ordner
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
