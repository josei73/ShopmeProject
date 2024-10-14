package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.admin.user.user.exception.ShippingRateNotFound;
import com.shopme.common.entity.admin.user.user.repository.StateRepository;
import com.shopme.common.entity.admin.user.user.service.ShippingRateService;
import com.shopme.common.model.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShippingRateRestController {


    @Autowired
    private ShippingRateService shippingRateService;



    @PostMapping("/get_shipping_cost")
    public String getShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFound {
        float shippingCost = shippingRateService.calculateShippingCost(productId, countryId, state);

        return String.valueOf(shippingCost);
    }

}
