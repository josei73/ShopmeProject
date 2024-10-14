package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Category;
import com.shopme.common.entity.admin.user.user.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;


    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics", "Electronics", "default.png");
        Category categorySaved = repository.save(category);
        assertThat(categorySaved.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory() {
        Category parent = new Category(7);
        Category subCategory = new Category("iPhone", parent);
        Category categorySaved = repository.save(subCategory);
        assertThat(categorySaved.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory() {
        var category = repository.findById(2).get();
        System.out.println(category.getName());
        var children = category.getChildren();
        for (Category cate : children) {
            System.out.println(cate.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories() {
        Iterable<Category> categories = repository.findAll();
        for (Category category : categories) {
            if (category.getParent() == null) {
                System.out.println(category.getName());
                var children = category.getChildren();
                for (Category child : children) {
                    System.out.println("-- " + child.getName());
                    printChildren(child,1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel){
        int newSubLevel = ++subLevel;
        for (Category child : parent.getChildren()) {
            for (int i = 0; i < newSubLevel ; ++i) {
                System.out.print("--");
            }
            System.out.println(child.getName());
            printChildren(child,newSubLevel);
        }
    }


    @Test
    public void testListRootCategories(){
        List<Category> rootCategories = repository.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(category -> System.out.println(category.getName()));
    }

    @Test
    public void testFindByName(){
        String name = "Computers";
        Category category = repository.findByName(name);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias(){
        String name = "electronics";
        Category category = repository.findByAlias(name);
        assertThat(category).isNotNull();
        assertThat(category.getAlias()).isEqualTo(name);
    }
}
