package com.shopme.common.entity.site.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.model.OrderReturnRequest;
import com.shopme.common.entity.site.model.OrderReturnResponse;
import com.shopme.common.entity.site.service.CustomerService;
import com.shopme.common.entity.site.service.OrderService;
import com.shopme.common.entity.site.util.Utility;
import com.shopme.common.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderRestController {

    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;


    @PostMapping("/orders/return")
    public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,HttpServletRequest servletRequest){
        Customer customer = getAuthenticatedCustomer(servletRequest);

        try {
            orderService.setOrderReturnRequested(returnRequest,customer);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }



        return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()),HttpStatus.OK);
    }



    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String emailOfAuthenticatedCustomer = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getByEmail(emailOfAuthenticatedCustomer);

    }
}
