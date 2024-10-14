package com.shopme.common.entity.site.controller;


import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.exception.AddressNotFoundException;
import com.shopme.common.entity.site.exception.CustomerNotFoundException;
import com.shopme.common.entity.site.service.AddressService;
import com.shopme.common.entity.site.service.CustomerService;
import com.shopme.common.entity.site.util.Utility;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<Address> addresses = addressService.listAddressBook(customer);
        boolean usePrimaryAddressAsDefault = true;
        for (Address address : addresses) {
            if (address.isDefaultForShipping()) {
                usePrimaryAddressAsDefault = false;
                break;
            }
        }

        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("addresses", addresses);
        model.addAttribute("customer", customer);
        return "address_book/addresses";
    }

    @GetMapping("/address_book/new")
    public String newAddress(Model model) {
        List<Country> countryList = addressService.listAllCountries();
        model.addAttribute("countries", countryList);
        model.addAttribute("pageTitle", "New Address");
        model.addAttribute("address", new Address());

        return "address_book/address_form";
    }


    @GetMapping("/address_book/edit/{id}")
    public String editAddress(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Address editAddress = addressService.get(id, getAuthenticatedCustomer(request));
            model.addAttribute("address", editAddress);
            model.addAttribute("pageTitle", "Edit Address (ID: " + id + ")");
            List<Country> countries = addressService.listAllCountries();
            model.addAttribute("countries", countries);
            return "address_book/address_form";
        } catch (AddressNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/address_book";
        }
    }

    @PostMapping("/address_book/save")
    public String saveAddress(@NotNull Address address, HttpServletRequest request, RedirectAttributes redirectAttributes) throws CustomerNotFoundException {

        address.setCustomer(getAuthenticatedCustomer(request));
        addressService.save(address);

        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/address_book";

         if("checkout".equals(redirectOption)) redirectURL += "?redirect=checkout";

        redirectAttributes.addFlashAttribute("message", "the address has been saved successfully. ");
        return redirectURL;
    }

    @GetMapping("/address_book/delete/{id}")
    public String deleteAddress(@PathVariable(name = "id") Integer id, HttpServletRequest request, RedirectAttributes redirectAttributes)  {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.delete(id, customer);
        redirectAttributes.addFlashAttribute("message", "The Address " + id + " has been deleted");
        return "redirect:/address_book";

    }


    @GetMapping("/address_book/default/{id}")
    public String setDefaultAddress(@PathVariable(name = "id") Integer id, HttpServletRequest request){
        addressService.setDefaultAddress(id,getAuthenticatedCustomer(request).getId());
        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/address_book";

        if ("cart".equals(redirectOption)) redirectURL = "redirect:/cart";
        else if("checkout".equals(redirectOption)) redirectURL = "redirect:/checkout";

        return redirectURL;
    }


    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String emailOfAuthenticatedCustomer = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getByEmail(emailOfAuthenticatedCustomer);

    }
}
