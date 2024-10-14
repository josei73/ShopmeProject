package com.shopme.common.entity.admin.user.user.controller;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.common.entity.admin.user.AmazonS3Util;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingParam;
import com.shopme.common.entity.admin.user.user.exception.UserNotFoundException;
import com.shopme.common.entity.admin.user.user.export.UserCsvExporter;
import com.shopme.common.entity.admin.user.user.export.UserExcelExporter;
import com.shopme.common.entity.admin.user.user.export.UserPdfExporter;
import com.shopme.common.entity.admin.user.user.service.UserService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    private String defaultRedirect = "redirect:/users/page/1?sortField=firstName&sortDir=asc";
    @Autowired
    private UserService service;




    @GetMapping("/users")
    public String listFirstPage() {
        return defaultRedirect;
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listUsers",moduleURL = "/users") PagingAndSortingHelper helper
            , @PathVariable(name = "pageNum") int pageNum) {

         service.listByPage(pageNum, helper);



        return "users/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {




        User user = new User();
        List<Role> listRoles = service.listRoles();
        model.addAttribute("user", user);
        user.setEnabled(true);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");
        return "users/user_form";
    }


    @PostMapping("/users/save")
    public String saveUser(@NotNull User user, RedirectAttributes redirectAttributes, @RequestParam("image")
    MultipartFile multipartFile) throws IOException {

        // User geht hier nicht rein, wenn kein Bild ausgewählt wurde
        if (!multipartFile.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(filename);
            User savedUser = service.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();

            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, filename, multipartFile.getInputStream());
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            service.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "the user " +user.getId() + " has been saved successfully. ");
        return getString(user);
    }

    private String getString(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        try {
            User editUser = service.get(id);
            model.addAttribute("user", editUser);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            List<Role> listRoles = service.listRoles();
            model.addAttribute("listRoles", listRoles);
            return "users/user_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return defaultRedirect;
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        try {
            service.delete(id);
            String uploadDir = "user-photos/" + id;
            AmazonS3Util.removeFolder(uploadDir);
            redirectAttributes.addFlashAttribute("message", "The user ID " +
                    id + " has been deleted successfully");
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return defaultRedirect;
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {

        service.updateUserEnableStatus(id, enabled);
        String status = enabled ? "enabled" : "disable";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return defaultRedirect;
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> listUsers = service.listAll();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = service.listAll();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<User> listUsers = service.listAll();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers, response);
    }




}
