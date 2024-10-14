package com.shopme.common.entity.site.controller;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.Settingmodel.EmailSettingBag;
import com.shopme.common.entity.site.oauth.CustomerOAuth2User;
import com.shopme.common.entity.site.security.CustomerUserDetails;
import com.shopme.common.entity.site.service.CustomerService;
import com.shopme.common.entity.site.service.SettingService;
import com.shopme.common.entity.site.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        List<Country> countryList = customerService.listAllCountries();
        model.addAttribute("countries", countryList);
        model.addAttribute("pageTitle", "Customer Registration");
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);
        model.addAttribute("PageTitle", "Registration Succeeded!");

        return "register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);

        String toAddress = customer.getEmail();
        String subject = emailSettingBag.getCustomerVerifySubject();
        String content = emailSettingBag.getCustomerVerifyContent();

        // Nutzen um E-Mails zu versenden
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        // DIE E-Mail festlegen, von wo die Verifizierung verschickt wie z. B. noreply@gmail.com und Sender ist dann Shopme
        helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        // Wie bei den anderen Verifizierungsseiten die richtigen Namen reinschreiben und die Mail mit den Verifizierungs code.
        content = content.replace("[[name]]", customer.getFullName());
        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);

    }


    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model) {
        boolean verified = customerService.verify(code);


        return "register/" + (verified ? "verify_success" : "verify_failed");
    }


    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getByEmail(email);
        List<Country> countries = customerService.listAllCountries();
        model.addAttribute("customer", customer);
        model.addAttribute("countries", countries);
        return "customer/account_form";
    }

    // Check wie der Kunde sich angemeldet hat, um dementsprechend die daten auszugeben


    @PostMapping("/update_account_details")
    public String updateAccountDetails(Customer customer, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        customerService.update(customer);
        redirectAttributes.addFlashAttribute("message", "Your Account has been update Successfully");

        updateNameForAuthenticatedCustomer(request, customer);
        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/account_details";

        if (redirectOption.equals("address_book")) redirectURL = "redirect:/address_book";
        else if (redirectOption.equals("cart")) redirectURL = "redirect:/cart";
        else if(redirectOption.equals("checkout")) redirectURL= "redirect:/address_book?redirect=checkout";

        return redirectURL;
    }

    private void updateNameForAuthenticatedCustomer(HttpServletRequest request, Customer customer) {
        // Merken UsernamePasswordAuthenticationToken und RememberMeAuthenticationToken gehören zur Klasse CustomerUserDetails
        // GOOGLE Facebook anmeldung und co... gehören zu  CustomerOAuth2User OAuth2AuthenticationToken Klasse
        Object principal = request.getUserPrincipal();

        if (principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
            userDetails.getCustomer().setFirstName(customer.getFirstName());
            userDetails.getCustomer().setLastName(customer.getLastName());


        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2Token.getPrincipal();
            oAuth2User.setFullName(customer.getFullName());
        }

    }

    private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
        CustomerUserDetails userDetails = null;
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        } else if (principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        }

        return userDetails;
    }

}
