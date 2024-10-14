package com.shopme.common.entity.site.oauth;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.service.CustomerService;
import com.shopme.common.enums.AuthenticationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    @Lazy
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authentication.getPrincipal();
        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = oAuth2User.getClientName();

        AuthenticationType authenticationType = getAuthentication(clientName);

        Customer customer = customerService.getByEmail(oAuth2User.getEmail());

        if (customer == null) {
            customerService.addNewCustomerUponOAuthLogin(name, email, countryCode,authenticationType);
        } else {
            customerService.updateAuthenticationType(customer, authenticationType);
            oAuth2User.setFullName(customer.getFullName());
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthentication(String clientName) {
        if (clientName.equals("Google")) return AuthenticationType.GOOGLE;

        else if (clientName.equals("Facebook")) return AuthenticationType.FACEBOOK;

        return AuthenticationType.DATABASE;
    }
}
