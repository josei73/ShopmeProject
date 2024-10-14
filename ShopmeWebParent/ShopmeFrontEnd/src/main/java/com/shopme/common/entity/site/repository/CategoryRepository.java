package com.shopme.common.entity.site.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    @Query("Select c from Category c where c.enabled = true order by c.name asc ")
    public List<Category> findAllEnabled();

    @Query("Select c from Category c where c.enabled = true And c.alias = ?1")
    public Category findAliasEnabled(String alias);
}
