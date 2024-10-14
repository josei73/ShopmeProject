package com.shopme.common.entity.admin.user;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.admin.user.user.repository.CategoryRepository;
import com.shopme.common.entity.admin.user.user.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
    // Erstellt ein Fake Objekt in runtime
    @MockBean
    private CategoryRepository repo;

    @InjectMocks
    private CategoryService service;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName() {
        Integer id = null;
        String name = "Computers";
        String alias = "abc";
        Category category = new Category(id, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(category);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateName");


    }


    @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias() {
        Integer id = null;
        String name = "abc";
        String alias = "computers";
        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);
        String result = service.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateAlias");


    }

    @Test
    public void testCheckUniqueInNewModeReturnOK() {
        Integer id = null;
        String name = "abc";
        String alias = "abc";
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("ok");
    }


    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName() {
        Integer id = 1;
        String name = "Computers";
        String alias = "abc";
        Category category = new Category(2, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(category);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateName");


    }


    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias() {
        Integer id = 1;
        String name = "abc";
        String alias = "computers";
        Category category = new Category(2, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);
        String result = service.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateAlias");
    }


    @Test
    public void testCheckUniqueInEditModeReturnOK() {
        Integer id = 1;
        String name = "abc";
        String alias = "abc";
        Category category = new Category(id, name, alias);
        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String result = service.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("ok");
    }


}
