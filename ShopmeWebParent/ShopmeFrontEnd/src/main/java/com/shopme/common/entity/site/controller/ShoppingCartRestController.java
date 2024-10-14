package com.shopme.common.entity.site.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.exception.CustomerNotFoundException;
import com.shopme.common.entity.site.exception.ShoppingCartException;
import com.shopme.common.entity.site.service.CustomerService;
import com.shopme.common.entity.site.service.ShoppingCartService;
import com.shopme.common.entity.site.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;


    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable("productId") Integer productId, @PathVariable("quantity") Integer quantity,
                                   HttpServletRequest request) {

        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updateQuantity = cartService.addProduct(productId, quantity, customer);
            return String.valueOf(updateQuantity) + "Item(s) of this product were added to your shopping cart.";

        } catch (CustomerNotFoundException e) {
            return "You must login to add this product to cart";
        } catch (ShoppingCartException ex) {
            return ex.getMessage();
        }


    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Integer productId, @PathVariable("quantity") Integer quantity,
                                 HttpServletRequest request) {


        try {
            Customer customer = getAuthenticatedCustomer(request);
            float subtotal = cartService.updateQuantity(productId, quantity, customer);

            return String.valueOf(subtotal);

        } catch (CustomerNotFoundException e) {
            return "You must login to change quantity of this product";
        }


    }


    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String emailOfAuthenticatedCustomer = Utility.getEmailOfAuthenticatedCustomer(request);
        if (emailOfAuthenticatedCustomer == null)
            throw new CustomerNotFoundException("No authenticated customer");

        return customerService.getByEmail(emailOfAuthenticatedCustomer);

    }


    @DeleteMapping("/cart/remove/{productId}")
    public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request) {

        try {
            Customer customer = getAuthenticatedCustomer(request);
            cartService.removeProduct(productId, customer);
            return "The Product has been remove from your shopping cart";

        } catch (CustomerNotFoundException e) {
            return "You must login to remove this product";
        }
    }
}
