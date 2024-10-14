package com.shopme.common.entity.site.controller;


import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.site.service.AddressService;
import com.shopme.common.entity.site.service.CustomerService;
import com.shopme.common.entity.site.service.ShippingRateService;
import com.shopme.common.entity.site.service.ShoppingCartService;
import com.shopme.common.entity.site.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;


    @Autowired private AddressService addressService;

    @Autowired private ShippingRateService shippingRateService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {

        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> items = cartService.listCartItems(customer);
        float estimatedTotal = 0.0F;
        for(CartItem cartItem : items){
            estimatedTotal+=cartItem.getSubtotal();
        }

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;
        // Wir nehmen eine default Address und nicht die primary und beim else heißt das wir nehmen die Primary Adresse
        if(defaultAddress != null) {
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }
        else{
            usePrimaryAddressAsDefault=true;
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }




        model.addAttribute("shippingSupported",shippingRate != null);
        model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
        model.addAttribute("cartItems", items);
        model.addAttribute("estimatedTotal", estimatedTotal);
        return "cart/shopping_cart";


    }



    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String emailOfAuthenticatedCustomer = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getByEmail(emailOfAuthenticatedCustomer);

    }

}
