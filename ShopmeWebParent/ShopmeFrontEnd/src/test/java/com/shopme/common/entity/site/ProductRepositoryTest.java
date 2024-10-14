package com.shopme.common.entity.site;


import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.site.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired private ProductRepository repository;

    @Test
    public void testFindByAlias(){
        String alias = "canon-eos-m50";
        Product product=repository.findByAlias(alias);
        assertThat(product).isNotNull();
    }

    @Test
    public void testFindById(){
        Integer id = 29;
        Product product=repository.findById(id).get();
        System.out.println(product);
        assertThat(product).isNotNull();
    }

}
