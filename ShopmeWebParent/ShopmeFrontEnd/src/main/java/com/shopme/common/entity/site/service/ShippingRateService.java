package com.shopme.common.entity.site.service;


import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.site.repository.ShippingRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateService {
    @Autowired private ShippingRateRepository rateRepository;

    // Wenn die Primary Adresse gebraucht wird deswegen auch Customer
    public ShippingRate getShippingRateForCustomer(Customer customer){
        String state = customer.getState();
        if(state == null || state.isEmpty())
            state = customer.getCity();

        return rateRepository.findByCountryAndState(customer.getCountry(),state);
    }

    // Hier wenn wir eine andere Adresse als default gewählt haben
    public ShippingRate getShippingRateForAddress(Address address){
        String state = address.getState();
        if(state == null || state.isEmpty())
            state = address.getCity();

        return rateRepository.findByCountryAndState(address.getCountry(),state);
    }

}
