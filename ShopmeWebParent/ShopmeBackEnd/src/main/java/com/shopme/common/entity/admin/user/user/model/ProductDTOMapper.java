package com.shopme.common.entity.admin.user.user.model;

import com.shopme.common.entity.product.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class ProductDTOMapper implements Function<Product, ProductDTO> {
    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(product.getName(), product.getMainImagePath(), product.getPrice(), product.getCost());
    }
}
