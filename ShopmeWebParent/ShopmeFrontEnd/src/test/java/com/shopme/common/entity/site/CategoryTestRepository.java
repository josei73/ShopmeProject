package com.shopme.common.entity.site;


import com.shopme.common.entity.site.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryTestRepository {

    @Autowired private CategoryRepository categoryRepository;



    @Test
    public void testListEnabledCategories(){
       var categories = categoryRepository.findAllEnabled();
       categories.forEach(category -> {
           System.out.println(category.getName()+" "+category.isEnabled());
       });
    }

    @Test
    public void testFindAlias(){
        String alias="electronics";
        var category = categoryRepository.findAliasEnabled(alias);
        assertThat(category).isNotNull();
    }
}
