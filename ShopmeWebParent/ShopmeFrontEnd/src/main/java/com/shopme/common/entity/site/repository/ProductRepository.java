package com.shopme.common.entity.site.repository;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("SELECT p from Product p where p.enabled=true and" +
            " (p.category.id = ?1 OR p.category.allParentIDs like %?2%) order by p.name asc ")
    public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

    public Product findByAlias(String alias);

    @Query(value = "SELECT * FROM products where enabled=true AND" +
            " Match(name,short_description,full_description) Against(?1)", nativeQuery = true)
    public Page<Product> search(String keyword,Pageable pageable);
}
