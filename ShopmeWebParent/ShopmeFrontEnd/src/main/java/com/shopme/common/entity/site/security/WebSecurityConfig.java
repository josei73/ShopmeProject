package com.shopme.common.entity.site.security;

import com.shopme.common.entity.site.oauth.CustomerOAuth2UserService;
import com.shopme.common.entity.site.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired private CustomerOAuth2UserService oAuth2UserService;
    @Autowired private OAuth2LoginSuccessHandler oAuth2LoginHandler;

    @Autowired private DatabaseLoginSuccessHandler databaseHandler;
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {



        http.authorizeRequests().
                antMatchers("/account_details","/update_account_details","/cart","/address_book/**","/checkout","/place_order","/process_paypal_order","/orders/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/login").usernameParameter("email").successHandler(databaseHandler).permitAll()
                .and()
                .oauth2Login().loginPage("/login")
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginHandler)
                .and()
                .logout().permitAll()
                .and().rememberMe().key("AbcDefgHijKlmnOpqrs_123456789011").tokenValiditySeconds(14 * 24 * 60 * 60)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //Damit ich die daten aus dem Ordner nutzen kann ignoriert er die authentication für diese Ordner
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
