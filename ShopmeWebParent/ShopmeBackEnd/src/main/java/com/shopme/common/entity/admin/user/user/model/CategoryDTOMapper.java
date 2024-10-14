package com.shopme.common.entity.admin.user.user.model;

import com.shopme.common.entity.Category;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryDTOMapper implements Function<Category,CategoryDTO> {

    @Override
    public CategoryDTO apply(Category category) {
        return new CategoryDTO(category.getId(),category.getName());
    }
}
