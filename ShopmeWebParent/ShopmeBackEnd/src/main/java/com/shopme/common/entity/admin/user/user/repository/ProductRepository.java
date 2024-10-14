package com.shopme.common.entity.admin.user.user.repository;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String name);

    @Query("Update Product p set p.enabled = ?2 Where p.id = ?1")
    @Modifying
    public void updateEnableStatus(Integer id, boolean enabled);
    public Long countById(Integer id);

    @Query("SELECT p from Product p WHERE CONCAT(p.name,' '," +
            "p.brand.name,' ',p.category.name) LIKE %?1%")
    public Page<Product> findAll(String keyword,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id= ?1 OR " +
            "p.category.allParentIDs LIKE %?2%")
    public Page<Product> findAllInCategory(Integer categoryId,String categoryIdMatch, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE (p.category.id= ?1 OR " +
            "p.category.allParentIDs LIKE %?2%) AND (p.name LIKE %?3% OR " +
            "p.shortDescription LIKE %?3% OR " +
            "p.fullDescription LIKE %?3% OR " +
            "p.brand.name LIKE %?3% OR " +
            "p.category.name LIKE %?3%) ")
    public Page<Product> searchInCategory(Integer categoryId,String categoryIdMatch,String keyword,Pageable pageable);

    @Query("select p from Product p where p.name like %?1%")
    public Page<Product> searchProductByName(String keyword,Pageable pageable);
}
