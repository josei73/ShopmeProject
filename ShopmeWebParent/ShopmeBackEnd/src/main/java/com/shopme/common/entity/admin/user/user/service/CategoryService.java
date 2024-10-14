package com.shopme.common.entity.admin.user.user.service;


import com.shopme.common.entity.Category;
import com.shopme.common.entity.admin.user.user.info.CategoryPageInfo;
import com.shopme.common.entity.admin.user.user.repository.CategoryRepository;
import com.shopme.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {
    public static final int Root_Categories_Per_Page = 4;

    @Autowired
    private CategoryRepository categoryRepo;

    public List<Category> listAllRoot() {
        return categoryRepo.findAll();
    }


    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("desc"))
            sort = sort.descending();
        else
            sort = sort.ascending();


        Pageable pageable = PageRequest.of(pageNum - 1, Root_Categories_Per_Page, sort);
        Page<Category> pageCategories = null;
        if (keyword != null && !keyword.isEmpty())
            pageCategories = categoryRepo.search(keyword, pageable);
        else
            pageCategories = categoryRepo.findRootCategories(pageable);


        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if (keyword != null && !keyword.isEmpty()) {
            List<Category> searchResult = pageCategories.getContent();
            for (Category category : searchResult) {
                category.setHasChildren((category.getChildren().size() > 0));
            }
            return searchResult;

        } else
            return listHierarchicalCategories(pageCategories.getContent(), sortDir);

    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();
        for (Category cat : rootCategories) {
            hierarchicalCategories.add(new Category(cat));
            var children = sortSubCategories(cat.getChildren(), sortDir);
            for (Category child : children) {
                var copyChild = new Category(child);
                copyChild.setName("--" + copyChild.getName());
                hierarchicalCategories.add(copyChild);
                listSubHierarchicalCategories(child, 1, sortDir, hierarchicalCategories);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(Category parent, int level, String sortDir, List<Category> hierarchicalCategories) {
        int newSubLevel = ++level;
        String name = "";
        var subChildren = sortSubCategories(parent.getChildren(), sortDir);
        for (Category subCategory : subChildren) {
            name = "";
            for (int i = 0; i < newSubLevel; ++i) {
                name += "--";
            }
            var copySubCategory = new Category(subCategory);
            copySubCategory.setName(name + copySubCategory.getName());
            hierarchicalCategories.add(copySubCategory);
            listSubHierarchicalCategories(subCategory, newSubLevel, sortDir, hierarchicalCategories);
        }
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB = categoryRepo.findRootCategories(Sort.by("name").ascending());
        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(category);
                var children = sortSubCategories(category.getChildren());
                for (Category child : children) {
                    var copyChild = new Category(child);
                    copyChild.setName("--" + copyChild.getName());
                    categoriesUsedInForm.add(copyChild);
                    listSubCategories(child, 1, categoriesUsedInForm);
                }
            }
        }

        return categoriesUsedInForm;
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

    }


    private void listSubCategories(Category parent, int subLevel, List<Category> categoriesUsedInForm) {
        int newSubLevel = ++subLevel;
        String name = "";

        var children = sortSubCategories(parent.getChildren());
        for (Category child : children) {
            name = "";
            for (int i = 0; i < newSubLevel; ++i) {
                name += "--";
            }
            var copySubCategory = new Category(child);
            copySubCategory.setName(name + copySubCategory.getName());
            categoriesUsedInForm.add(copySubCategory);
            listSubCategories(child, newSubLevel, categoriesUsedInForm);
        }
    }

    public Category save(Category category) {
        Category parent = category.getParent();
        if (parent != null) {
            String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIds += String.valueOf(parent.getId()) + "-";
            category.setAllParentIDs(allParentIds);
        }
        return categoryRepo.save(category);
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = categoryRepo.findByName(name);

        if (isCreatingNew) {
            if (categoryByName != null)
                return "DuplicateName";
            else {
                if (categoryRepo.findByAlias(alias) != null)
                    return "DuplicateAlias";
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id)
                return "DuplicateName";
            var categoryByAlias = categoryRepo.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id)
                return "DuplicateAlias";
        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc"))
                    return cat1.getName().compareTo(cat2.getName());
                else
                    return cat2.getName().compareTo(cat1.getName());
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }


    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepo.countById(id);
        if (countById == null | countById == 0)
            throw new CategoryNotFoundException("Could not find any category with ID" + id);

        categoryRepo.deleteById(id);
    }


    public void updateUserEnableStatus(Integer id, boolean enabled) {
        categoryRepo.updateEnableStatus(id, enabled);
    }
}
