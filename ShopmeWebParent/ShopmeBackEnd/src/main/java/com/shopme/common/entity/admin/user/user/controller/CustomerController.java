package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.*;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingParam;
import com.shopme.common.entity.admin.user.user.exception.CustomerNotFoundException;
import com.shopme.common.entity.admin.user.user.service.CustomerService;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;


    @GetMapping("/customers")
    public String listFirstPage(Model model) {
        return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "customers", moduleURL = "/customers") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum) {

        customerService.listByPage(pageNum, helper);

        return "customers/customers";
    }


    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer editCustomer = customerService.get(id);
            model.addAttribute("customer", editCustomer);
            model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
            List<Country> countries = customerService.listAllCountries();
            model.addAttribute("countries", countries);
            return "customers/customer_form";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            customerService.delete(id);
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {

        customerService.updateEnableStatus(id, enabled);
        String status = enabled ? "enabled" : "disable";
        String message = "The customer ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/customers";
    }

    @PostMapping("/customers/save")
    public String saveUser(@NotNull Customer customer, RedirectAttributes redirectAttributes) throws IOException {
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", "the Customer " + customer.getId() + " has been saved successfully. ");
        return "redirect:/customers";
    }


    @GetMapping("/customers/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.get(id);
            model.addAttribute("customer", customer);
            return "customers/customer_detail_modal";
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/customers";
        }
    }


}
