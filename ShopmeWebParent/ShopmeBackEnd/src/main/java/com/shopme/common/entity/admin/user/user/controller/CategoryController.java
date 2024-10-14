package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.Category;

import com.shopme.common.entity.admin.user.AmazonS3Util;
import com.shopme.common.entity.admin.user.FileUpdateUtil;
import com.shopme.common.entity.admin.user.user.export.CategoryCsvExporter;
import com.shopme.common.entity.admin.user.user.info.CategoryPageInfo;
import com.shopme.common.entity.admin.user.user.service.CategoryService;
import com.shopme.common.exception.CategoryNotFoundException;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

@Controller
public class CategoryController {

    private String defaultRedirect = "redirect:/categories/page/1?sortField=name&sortDir=asc";

    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
      return listByPage(1,sortDir,null,model);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model){
        if (sortDir == null || sortDir.isEmpty())
            sortDir = "asc";
        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> categories = service.listByPage(pageInfo,pageNum,sortDir,keyword);

        String reverseSortDir = sortDir.equals("desc") ? "asc" : "desc";
        long startCount = (pageNum - 1) * CategoryService.Root_Categories_Per_Page + 1;
        long endCount = startCount + CategoryService.Root_Categories_Per_Page - 1;

        if (endCount > pageInfo.getTotalElements()) endCount = pageInfo.getTotalElements();


        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages",pageInfo.getTotalPages());
        model.addAttribute("totalItems",pageInfo.getTotalElements());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword",keyword);
        model.addAttribute("categories", categories);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("moduleURL","/categories");
        return "categories/categories";
    }


    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        Category category = new Category();
        List<Category> categories = service.listCategoriesUsedInForm();

        model.addAttribute("category", category);
        category.setEnabled(true);
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
    }


    @PostMapping("/categories/save")
    public String saveCategory(@NotNull Category category, @RequestParam("fileImage")
    MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category savedCategory = service.save(category);
            String uploadDir = "categories-images/" + savedCategory.getId();
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
            redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
        } else {
            if (category.getImage().isEmpty()) category.setImage(null);
            service.save(category);

        }
        redirectAttributes.addFlashAttribute("message", "the category " +category.getId() + " has been saved successfully. ");
        return defaultRedirect;
    }


    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes redirectAttributes) {

        try {
            Category editCategory = service.get(id);
            model.addAttribute("category", editCategory);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
            List<Category> categories = service.listCategoriesUsedInForm();
            model.addAttribute("categories", categories);
            return "categories/category_form";
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }

    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        service.updateUserEnableStatus(id, enabled);
        String status = enabled ? "enabled" : "disable";
        String message = "The category ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return defaultRedirect;
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws CategoryNotFoundException {

        try {
            service.delete(id);
            String uploadDir = "categories-images/" + id;
            AmazonS3Util.removeFolder(uploadDir);
            redirectAttributes.addFlashAttribute("message", "The category ID " +
                    id + " has been deleted successfully");
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirect;
    }


    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> listCategories = service.listCategoriesUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(listCategories, response);
    }


}
