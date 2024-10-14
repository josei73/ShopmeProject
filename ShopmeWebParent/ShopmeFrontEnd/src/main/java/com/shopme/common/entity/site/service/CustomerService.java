package com.shopme.common.entity.site.service;


import com.shopme.common.entity.Country;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.exception.CustomerNotFoundException;
import com.shopme.common.entity.site.repository.CountryRepository;
import com.shopme.common.entity.site.repository.CustomerRepository;
import com.shopme.common.enums.AuthenticationType;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email) {

        return customerRepository.findByEmail(email) == null;
    }

    public void registerCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());


        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        customerRepository.save(customer);

    }

    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode, AuthenticationType type) {
        Customer customer = new Customer();
        customer.setEmail(email);
        setName(name, customer);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(type);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setPostalCode("");
        customer.setState("");
        customer.setPhoneNumber("");
        customer.setCountry(countryRepository.findByCode(countryCode));

        customerRepository.save(customer);
    }


    public void update(Customer customerInForm) {
        Customer customerInDB = customerRepository.findById(customerInForm.getId()).get();
        if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (!customerInForm.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
                customerInForm.setPassword(encodedPassword);
            } else {

                customerInForm.setPassword(customerInDB.getPassword());
            }
        } else
            customerInForm.setPassword(customerInDB.getPassword());

        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedTime(customerInDB.getCreatedTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

        customerRepository.save(customerInForm);
    }


    private void setName(String name, Customer customer) {
        String[] nameArray = name.split(" ");
        if (nameArray.length < 2) {
            customer.setFirstName(name);
            customer.setLastName(" ");
        } else {
            String firstName = nameArray[0];
            customer.setFirstName(firstName);
            String lastName = name.replace(firstName + " ", "");

            customer.setLastName(lastName);
        }

    }


    public void encodePassword(Customer customer) {
        String encoder = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encoder);
    }

    public boolean verify(String verificationCode) {
        Customer customer = customerRepository.findByVerificationCode(verificationCode);
        if (customer == null || customer.isEnabled())
            return false;
        customerRepository.enabled(customer.getId());
        return true;
    }

    public void updateAuthenticationType(Customer customer, AuthenticationType type) {
        if (customer.getAuthenticationType() != type)
            customerRepository.updateAuthenticationType(customer.getId(), type);
    }

    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
        Customer customer = getByEmail(email);
        if(customer != null){
            String token = RandomString.make(30);
            customer.setResetPasswordToken(token);
            customerRepository.save(customer);
            return token;
        }
        throw new CustomerNotFoundException("Could not find any customer with the email "+email);
    }


    public Customer getByResetPasswordToken(String token){
        return customerRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
        Customer customerByToken = customerRepository.findByResetPasswordToken(token);
        if(customerByToken == null)
            throw new CustomerNotFoundException("No customer found Invalid Token "+token);

       customerByToken.setPassword(newPassword);
       encodePassword(customerByToken);
       customerByToken.setResetPasswordToken(null);
       customerRepository.save(customerByToken);
    }
}
