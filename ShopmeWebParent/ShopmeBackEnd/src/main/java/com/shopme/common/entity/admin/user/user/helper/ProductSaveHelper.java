package com.shopme.common.entity.admin.user.user.helper;

import com.shopme.common.entity.admin.user.AmazonS3Util;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
import com.shopme.common.entity.admin.user.FileUpdateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductSaveHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

   public static void deleteExtraImagesWereRemoved(Product product) {
        String extraImage = "product-images/" + product.getId() + "/extras";
       List<String> listObjectKeys = AmazonS3Util.listFolder(extraImage);

       for (String objectKey : listObjectKeys){
           int lastIndexOfSlash = objectKey.lastIndexOf("/");
           String fileName = objectKey.substring(lastIndexOfSlash+1,objectKey.length());

           if (!product.containsImageName(fileName)) {
               AmazonS3Util.deleteFile(objectKey);
               System.out.println("Deleted Extra Image Objekt Key");
           }
       }

    }

    public static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0)
            return;

        Set<ProductImage> images = new HashSet<>();

        for (int i = 0; i < imageIDs.length; i++) {
            Integer id = Integer.parseInt(imageIDs[i]);
            String name = imageNames[i];
            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    public static void setProductDetails(Product product, String[] detailIDs, String[] detailsName, String[] detailsValue) {
        if (detailsName == null || detailsName.length == 0) return;

        String name;
        String value;
        for (int i = 0; i < detailsName.length; i++) {
            name = detailsName[i];
            value = detailsValue[i];
            Integer id = Integer.parseInt(detailIDs[i]);
            if (id != 0) {
                product.addDetail(id, name, value);
            } else if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }

        }
    }

    public static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultipart, Product savedProduct) throws IOException {

        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "product-images/" + savedProduct.getId() ;

            // Bevor neue Bilder eingespeichert werden, müssen die alten Pics gelöscht werden.
            List<String> objectKeys = AmazonS3Util.listFolder(uploadDir+"/");

            for (String objectKey : objectKeys){
                if(!objectKey.contains("/extras/")){
                    AmazonS3Util.deleteFile(objectKey);
                }
            }
            System.out.println(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, mainImageMultipart.getInputStream());
        }

        if (extraImageMultipart.length > 0) {
            String productImageDir = "product-images/" + savedProduct.getId() + "/extras";
            String fileName = "";
            for (MultipartFile multipartFile : extraImageMultipart) {
                if (!multipartFile.isEmpty()) {
                    fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    AmazonS3Util.uploadFile(productImageDir, fileName, multipartFile.getInputStream());

                }
            }
        }
    }


    public static void setNewExtraImageNames(Product product, MultipartFile[] extraImageMultipart) {
        if (extraImageMultipart.length > 0) {
            String fileName = "";
            for (MultipartFile multipartFile : extraImageMultipart) {
                if (!multipartFile.isEmpty()) {
                    fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    if (!product.containsImageName(fileName))
                        product.addExtraImage(fileName);
                }
            }
        }
    }

    public static void setMainImageName(Product product, MultipartFile mainImageMultipart) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }


}
