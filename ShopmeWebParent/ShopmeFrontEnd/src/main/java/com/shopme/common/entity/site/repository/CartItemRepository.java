package com.shopme.common.entity.site.repository;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    public List<CartItem> findByCustomer(Customer customer);
    public CartItem findByCustomerAndProduct(Customer customer, Product product);
    public void deleteByCustomerIdAndProductId(Integer customerId,Integer productId);

    @Query("UPDATE CartItem c set c.quantity= ?1 where c.customer.id = ?2 AND c.product.id=?3 ")
    @Modifying
    public void updateQuantity(Integer quantity,Integer customerId,Integer productId);

    public void deleteByCustomerId(Integer customerId);
}
