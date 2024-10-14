package com.shopme.common.entity.site.service;


import com.shopme.common.entity.Category;
import com.shopme.common.entity.site.repository.CategoryRepository;
import com.shopme.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;


    public List<Category> listNoChildrenCategories() {
        List<Category> listNoChildrenCategories = new ArrayList<>();
        List<Category> enabledCategories = repository.findAllEnabled();

        enabledCategories.forEach(category -> {
            if (category.getChildren() == null || category.getChildren().size() == 0)
                listNoChildrenCategories.add(category);
        });
        return listNoChildrenCategories;

    }

    public Category getCategory(String alias) throws CategoryNotFoundException {
       Category category=repository.findAliasEnabled(alias);
       if(category == null)
           throw new CategoryNotFoundException("Could not find any categories with alias "+alias);
       return category;
    }

    public List<Category> getCategoryParents(Category child) {
        List<Category> listParents = new ArrayList<>();

        Category parent = child.getParent();
        while (parent != null) {
            listParents.add(0, parent);
            parent = parent.getParent();
        }
        listParents.add(child);
        return listParents;
    }
}
