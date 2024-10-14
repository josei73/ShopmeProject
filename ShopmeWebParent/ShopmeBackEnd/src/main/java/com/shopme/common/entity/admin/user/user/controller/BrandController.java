package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.admin.user.AmazonS3Util;
import com.shopme.common.entity.admin.user.FileUpdateUtil;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingParam;
import com.shopme.common.entity.admin.user.user.exception.BrandNotFoundExcpetion;
import com.shopme.common.entity.admin.user.user.export.BrandCsvExporter;
import com.shopme.common.entity.admin.user.user.service.BrandsService;
import com.shopme.common.entity.admin.user.user.service.CategoryService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class BrandController {
   private String defaultRedirect = "redirect:/brands/page/1?sortField=name&sortDir=asc";

    @Autowired
    private BrandsService brandService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/brands")
    public String listFirstPage() {
        return "redirect:/brands/page/1?sortField=name&sortDir=asc";
    }

    @GetMapping("/brands/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "brands", moduleURL = "/brands") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum) {
        brandService.listByPage(pageNum, helper);


        return "brands/brands";
    }


    @GetMapping("/brands/new")
    public String newBrand(Model model) {
        Brand brand = new Brand();
        List<Category> categories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("brand", brand);

        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Create New Brand");
        return "brands/brand_form";
    }


    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, Model model,
                            RedirectAttributes redirectAttributes) {

        try {
            Brand editBrand = brandService.get(id);
            model.addAttribute("brand", editBrand);
            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
            List<Category> categories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("categories", categories);
            return "brands/brand_form";
        } catch (BrandNotFoundExcpetion ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return defaultRedirect;
        }

    }


    @PostMapping("/brands/save")
    public String saveCategory(@NotNull Brand brand, @RequestParam("fileImage")
    MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand savedBrand = brandService.save(brand);
            String uploadDir = "brand-logos/" + savedBrand.getId();
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
            redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully.");
        } else {
            if (brand.getLogo().isEmpty()) brand.setLogo(null);
            brandService.save(brand);

        }
        redirectAttributes.addFlashAttribute("message", "the brand " + brand.getId() + " has been saved successfully. ");
        return defaultRedirect;
    }


    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws BrandNotFoundExcpetion {

        try {
            brandService.delete(id);
            String uploadDir = "brand-logos/" + id;
            AmazonS3Util.removeFolder(uploadDir);
            redirectAttributes.addFlashAttribute("message", "The Brand ID " +
                    id + " has been deleted successfully");
        } catch (BrandNotFoundExcpetion ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirect;
    }


    @GetMapping("/brands/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Brand> listCategories = brandService.listAll();
        BrandCsvExporter exporter = new BrandCsvExporter();
        exporter.export(listCategories, response);
    }


}
