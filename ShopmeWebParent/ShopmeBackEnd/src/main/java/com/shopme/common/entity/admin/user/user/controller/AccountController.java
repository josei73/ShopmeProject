package com.shopme.common.entity.admin.user.user.controller;

import com.shopme.common.entity.User;
import com.shopme.common.entity.admin.user.AmazonS3Util;
import com.shopme.common.entity.admin.user.FileUpdateUtil;
import com.shopme.common.entity.admin.user.security.ShopmeUserDetails;
import com.shopme.common.entity.admin.user.user.service.UserService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    // ohne AuthenticationPrincipal kann ich nicht auf UserDetails zugreifen
    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model) {
        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);
        model.addAttribute("user", user);
        return "users/account_form";
    }

    @PostMapping("/account/update")
    public String saveDetails(@NotNull User user, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                              @RequestParam("image")
                              MultipartFile multipartFile) throws IOException {


        if (!multipartFile.isEmpty()) {
            String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(filename);
            User savedUser = userService.updateAccount(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            //Selbst geschriebene Klasse
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir,filename,multipartFile.getInputStream());
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastname(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "Your Account details have been updated. ");
        return "redirect:/account";
    }
}
