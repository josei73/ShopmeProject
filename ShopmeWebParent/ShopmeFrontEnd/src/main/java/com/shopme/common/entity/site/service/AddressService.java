package com.shopme.common.entity.site.service;


import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.exception.AddressNotFoundException;
import com.shopme.common.entity.site.repository.AddressRepository;
import com.shopme.common.entity.site.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AddressService {

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private CountryRepository countryRepo;


    public List<Country> listAllCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }


    public List<Address> listAddressBook(Customer customer) {
        return addressRepo.findByCustomer(customer);
    }

    public Address get(Integer id, Customer customer) throws AddressNotFoundException {
        return addressRepo.findByIdAndCustomerId(id, customer.getId());

    }

    public void save(Address rateInForm) {
        addressRepo.save(rateInForm);
    }

    public void delete(Integer addressId, Customer customer) {
        addressRepo.deleteByIdAndCustomerId(addressId, customer.getId());
    }

    public void setDefaultAddress(Integer addressId, Integer customerId) {
        if (addressId > 0) {
            addressRepo.setDefaultAddress(addressId);

        }
        addressRepo.setNonDefaultForOthers(addressId, customerId);
    }

    public Address getDefaultAddress(Customer customer) {
        return addressRepo.findDefaultCustomer(customer.getId());
    }


}
