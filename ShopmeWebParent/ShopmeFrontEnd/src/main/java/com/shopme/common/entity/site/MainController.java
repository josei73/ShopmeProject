package com.shopme.common.entity.site;


import com.shopme.common.entity.Category;
import com.shopme.common.entity.site.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired private CategoryService service;

    @GetMapping("")
    public String vieHomePage(Model model){

        List<Category> listNoChildrenCategories = service.listNoChildrenCategories();
        model.addAttribute("categories",listNoChildrenCategories);

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
