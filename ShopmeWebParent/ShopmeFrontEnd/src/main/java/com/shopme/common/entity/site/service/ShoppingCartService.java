package com.shopme.common.entity.site.service;


import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.site.exception.ShoppingCartException;
import com.shopme.common.entity.site.repository.CartItemRepository;
import com.shopme.common.entity.site.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ShoppingCartService {
    @Autowired
    private CartItemRepository cartRepo;
    @Autowired private ProductRepository productRepo;


    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
        Integer updatedQuantity = quantity;
        Product product = new Product(productId);
        CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);

        if (cartItem != null) {
            updatedQuantity = cartItem.getQuantity() + quantity;
            if (updatedQuantity > 5)
                throw new ShoppingCartException("Could not add more" + quantity + " item(s)" +
                        "because there's already " + cartItem.getQuantity() + " item(s)" +
                        "in your shopping cart. Maximum allowed quantity is 5");
        } else {
            cartItem = new CartItem(quantity);
            cartItem.setProduct(product);
            cartItem.setCustomer(customer);
        }
        cartItem.setQuantity(updatedQuantity);
        cartRepo.save(cartItem);

        return updatedQuantity;
    }


    public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
        cartRepo.updateQuantity(quantity, customer.getId(), productId);
        Product product = productRepo.findById(productId).get();
        return product.getDiscountPrice()*quantity;
    }

    public List<CartItem> listCartItems(Customer customer) {
        return cartRepo.findByCustomer(customer);
    }


    public void removeProduct(Integer productId,Customer customer){
        cartRepo.deleteByCustomerIdAndProductId(customer.getId(),productId);
    }


    public void deleteByCustomer(Customer customer){
        cartRepo.deleteByCustomerId(customer.getId());
    }
}
