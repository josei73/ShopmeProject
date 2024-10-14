package com.shopme.common.entity.admin.user.user.service;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.user.exception.CustomerNotFoundException;
import com.shopme.common.entity.admin.user.user.repository.CountryRepository;
import com.shopme.common.entity.admin.user.user.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {

    public static final int CUSTOMER_PER_PAGE = 10;

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    PasswordEncoder passwordEncoder;


    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Integer id, String email) {
        Customer existCustomer = customerRepo.findByEmail(email);

        if (existCustomer != null && existCustomer.getId() != id)
            return false;


        return true;
    }


    public void save(Customer customerInForm) {
        Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
        if (!customerInForm.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodedPassword);
        } else {

            customerInForm.setPassword(customerInDB.getPassword());
        }
        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedTime(customerInDB.getCreatedTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

        customerRepo.save(customerInForm);
    }


    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum,CUSTOMER_PER_PAGE,customerRepo);
    }


    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CustomerNotFoundException("Could not find any customer with ID" + id);
        }
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long countById = customerRepo.countById(id);
        if (countById == null | countById == 0)
            throw new CustomerNotFoundException("Could not find any customer with ID" + id);
        customerRepo.deleteById(id);
    }

    public void updateEnableStatus(Integer id, boolean enabled) {
        customerRepo.updateEnableStatus(id, enabled);
    }
}
