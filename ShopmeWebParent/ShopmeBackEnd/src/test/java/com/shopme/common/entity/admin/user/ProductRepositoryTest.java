package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.admin.user.user.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;


    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 37);
        Category category = entityManager.find(Category.class, 5);
        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("Short description for Acer Aspire");
        product.setFullDescription("Full description for Acer Aspire");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(678);
        product.setCost(600);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProdcut = repository.save(product);

        assertThat(savedProdcut).isNotNull();
        assertThat(savedProdcut.getId()).isGreaterThan(0);

    }

    @Test
    public void testListAllProducts() {
        List<Product> products = repository.findAll();

        products.forEach(product -> System.out.println(product));
    }

    @Test
    public void testGetProduct() {
        Integer id = 2;
        Product product = repository.findById(id).get();
        System.out.println(product);
        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct() {
        Integer id = 1;
        Product product = repository.findById(id).get();
        product.setPrice(499);
        repository.save(product);
        Product updatedProduct = entityManager.find(Product.class, id);
        assertThat(updatedProduct.getPrice()).isEqualTo(499);
    }

    @Test
    public void testDeleteProduct() {
        Integer id = 3;
        repository.deleteById(3);
        Optional<Product> product = repository.findById(id);
        assertThat(!product.isPresent());
    }

    @Test
    public void testFindByName() {
        String name = "Samsung Galaxy Tab A. 8.0 32 GB";
        Product product = repository.findByName(name);
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    public void testSaveProductWithImages() {
        Integer productId = 1;
        Product product = repository.findById(productId).get();

        product.setMainImage("main.png");
        var savedProduct = repository.save(product);
        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    public void testSaveProductWithDetails() {
        Integer id = 1;
        Product product = repository.findById(id).get();
        product.addDetail("Device Memory", "128 GB");
        product.addDetail("CPU Model", "MediaTek");
        product.addDetail("OS", "Android 10");
        var savedProduct = repository.save(product);
        assertThat(savedProduct.getDetails()).isNotEmpty();
    }



}
