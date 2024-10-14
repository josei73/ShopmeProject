package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.admin.user.user.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository repository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateBrand() {
        Category category = entityManager.find(Category.class, 6);
        Brand brand = new Brand("Acer", "brand-logo.png");
        brand.addCategory(category);
        Brand savedBrand = repository.save(brand);
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }


    @Test
    public void testCreateNewBrandWithTwoCategories() {
        Brand brand = new Brand("Apple", "brand-logo.png");
        Category cellPhonesCategory = entityManager.find(Category.class, 4);
        Category tabletCategory = entityManager.find(Category.class, 7);
        brand.addCategory(cellPhonesCategory);
        brand.addCategory(tabletCategory);
        Brand savedBrand = repository.save(brand);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAllBrands(){
        List<Brand> brands = repository.findAll();
        brands.forEach(brand -> System.out.println(brand));
        assertThat(brands.size()).isGreaterThan(0);
    }

    @Test
    public void testGetByID(){
        Brand brand = repository.findById(1).get();
        System.out.println(brand);
        assertThat(brand).isNotNull();
    }

    @Test
    public void testUpdateBrand(){
        String name="Samsung Electronics";
        Brand brand = repository.findById(3).get();
        brand.setName(name);
        System.out.println(brand);
        repository.save(brand);
    }
    @Test
    public void deleteBrand(){
       Integer id = 2;
       repository.deleteById(id);
    }

    @Test
    public void testFindByName(){
        String name = "Acer";
        Brand brand = repository.findByName(name);
        assertThat(brand).isNotNull();
        assertThat(brand.getName()).isEqualTo(name);
    }
}
