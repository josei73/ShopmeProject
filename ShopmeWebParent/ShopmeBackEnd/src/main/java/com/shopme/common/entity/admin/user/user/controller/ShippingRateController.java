package com.shopme.common.entity.admin.user.user.controller;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingParam;
import com.shopme.common.entity.admin.user.user.exception.CustomerNotFoundException;
import com.shopme.common.entity.admin.user.user.exception.ShippingAlreadyExistException;
import com.shopme.common.entity.admin.user.user.exception.ShippingRateNotFound;
import com.shopme.common.entity.admin.user.user.service.ShippingRateService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class ShippingRateController {
    private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
    @Autowired
    private ShippingRateService shippingRateService;


    @GetMapping("/shipping_rates")
    public String listFirstPage() {
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "rates", moduleURL = "/shipping_rates") PagingAndSortingHelper helper
            , @PathVariable(name = "pageNum") int pageNum) {
        shippingRateService.listByPage(pageNum, helper);
        return "shipping/shipping_rates";
    }


    @PostMapping("/shipping_rates/save")
    public String saveShippingRate(@NotNull ShippingRate shippingRate, RedirectAttributes redirectAttributes) {

        try {
            shippingRateService.save(shippingRate);
            redirectAttributes.addFlashAttribute("message", "the Shipping rate " + shippingRate.getId() + " has been saved successfully. ");
        } catch (ShippingAlreadyExistException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

        }

        return defaultRedirectURL;

    }

    @GetMapping("/shipping_rates/new/")
    public String newShippingRate(Model model) {


        model.addAttribute("shippingRate", new ShippingRate());
        model.addAttribute("pageTitle", "New Rate");
        List<Country> countries = shippingRateService.listAllCountries();
        model.addAttribute("countries", countries);
        return "shipping/shipping_rate_form";

    }


    @GetMapping("/shipping_rates/edit/{id}")
    public String editShippingRate(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ShippingRate editShippingRate = shippingRateService.get(id);
            model.addAttribute("shippingRate", editShippingRate);
            model.addAttribute("pageTitle", "Edit Shipping Rate (ID: " + id + ")");
            List<Country> countries = shippingRateService.listAllCountries();
            model.addAttribute("countries", countries);
            return "shipping/shipping_rate_form";
        } catch (ShippingRateNotFound ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/shipping_rates";
        }
    }

    @GetMapping("/shipping_rates/delete/{id}")
    public String deleteShippingRate(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            shippingRateService.delete(id);
        } catch (ShippingRateNotFound ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
    public String updateRateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("supported") boolean supported,
                                          RedirectAttributes redirectAttributes) {

        try {
            shippingRateService.updateUserEnableStatus(id, supported);
            String status = supported ? "enabled" : "disable";
            String message = "The shipping rate ID " + id + " has been " + status;
            redirectAttributes.addFlashAttribute("message", message);
        } catch (ShippingRateNotFound e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return defaultRedirectURL;

    }
}
