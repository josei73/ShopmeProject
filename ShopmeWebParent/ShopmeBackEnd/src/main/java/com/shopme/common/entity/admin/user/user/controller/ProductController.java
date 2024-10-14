package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.admin.user.AmazonS3Util;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.admin.user.FileUpdateUtil;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingParam;
import com.shopme.common.entity.admin.user.security.ShopmeUserDetails;
import com.shopme.common.entity.admin.user.user.helper.ProductSaveHelper;
import com.shopme.common.entity.admin.user.user.service.BrandsService;
import com.shopme.common.entity.admin.user.user.service.CategoryService;
import com.shopme.common.entity.admin.user.user.service.ProductService;
import com.shopme.common.exception.ProductNotFoundException;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Controller
@Transactional
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private String defaultRedirect = "redirect:/products/page/1?sortField=name&sortDir=asc";

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandsService brandsService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/products")
    public String listFirstPage() {
        return "redirect:/products/page/1?sortField=name&sortDir=asc";
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "products", moduleURL = "/products") PagingAndSortingHelper helper, @PathVariable(name = "pageNum") int pageNum,
                             @Param("categoryId") Integer categoryId, Model model) {

        productService.listByPage(pageNum, helper, categoryId);
        List<Category> categories = categoryService.listCategoriesUsedInForm();
        if (categories != null) model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categories);


        return "products/products";
    }


    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> brands = brandsService.listAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);
        model.addAttribute("product", product);
        model.addAttribute("brands", brands);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberOfExistingExtraImages", 0);
        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@NotNull Product product, @RequestParam(value = "fileImage", required = false)
    MultipartFile mainImageMultipart, @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultipart, RedirectAttributes redirectAttributes
            , @RequestParam(name = "detailNames", required = false) String[] detailsName
            , @RequestParam(name = "detailValues", required = false) String[] detailsValue,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @AuthenticationPrincipal ShopmeUserDetails loggedUser) throws IOException {


        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Salesperson")) {
                productService.saveProductPrice(product);
                redirectAttributes.addFlashAttribute("message", "The Product has been saved successfully");
                return defaultRedirect;
            }
        }
        ProductSaveHelper.setMainImageName(product, mainImageMultipart);
        ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
        ProductSaveHelper.setNewExtraImageNames(product, extraImageMultipart);
        ProductSaveHelper.setProductDetails(product, detailIDs, detailsName, detailsValue);


        Product savedProduct = productService.save(product);

        ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProduct);
        ProductSaveHelper.deleteExtraImagesWereRemoved(product);
        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");


        return defaultRedirect;
    }


    @GetMapping("/products/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {
        productService.updateUserEnableStatus(id, enabled);
        String status = enabled ? "enabled" : "disable";
        String message = "The product ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return defaultRedirect;
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws ProductNotFoundException {

        try {
            productService.delete(id);
            String productExtraImageDir = "product-images/" + id + "/extras";
            String uploadDir = "product-images/" + id;
            AmazonS3Util.removeFolder(productExtraImageDir);
            AmazonS3Util.removeFolder(uploadDir);
            redirectAttributes.addFlashAttribute("message", "The product ID " +
                    id + " has been deleted successfully");
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirect;
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
        try {
            Product product = productService.get(id);
            model.addAttribute("product", product);
            List<Brand> brands = brandsService.listAll();
            Integer numberOfExistingExtraImages = product.getImages().size();
            boolean isReadOnlyForSalesperson = false;
            if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if (loggedUser.hasRole("Salesperson")) {
                   isReadOnlyForSalesperson = true;
                }
            }
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + " )");
            model.addAttribute("brands", brands);
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            model.addAttribute("isReadOnlyForSalesperson",isReadOnlyForSalesperson);
            return "products/product_form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return defaultRedirect;
        }
    }


    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);
            model.addAttribute("product", product);
            return "products/product_detail_modal";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return defaultRedirect;
        }
    }
}
