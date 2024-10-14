package com.shopme.common.entity.admin.user.user.controller;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String viewLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Hier weiß ich das er sich nicht angemeldet hat
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            return "login";

        return "redirect:/";
    }
}
