package com.shopme.common.entity.site;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.site.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveItem() {
        Integer customerId = 1;
        Integer productId = 4;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);
        CartItem cartItem = new CartItem(5);
        cartItem.setCustomer(customer);
        cartItem.setProduct(product);
        CartItem savedItem = repository.save(cartItem);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isGreaterThan(0);


    }

    @Test
    public void testSaveTwoItem() {
        Integer customerId = 10;
        Integer productId = 10;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);
        CartItem cartItem = new CartItem(3);
        cartItem.setCustomer(customer);
        cartItem.setProduct(product);


        customer = entityManager.find(Customer.class, 15);
        product = entityManager.find(Product.class, 15);
        CartItem cartItem2 = new CartItem(4);

        cartItem2.setCustomer(customer);
        cartItem2.setProduct(product);
        Iterable<CartItem> iterable = repository.saveAll(List.of(cartItem, cartItem2));


        assertThat(iterable).hasSizeGreaterThan(0);

    }

    @Test
    public void testFindByCustomer() {
        Integer customerId = 10;
        // Integer productId = 10;

        Customer customer = entityManager.find(Customer.class, customerId);
        List<CartItem> items = repository.findByCustomer(customer);
        items.forEach(System.out::println);
        assertThat(items.size()).isEqualTo(2);
    }

    @Test
    public void testFindByCustomerNProduct() {
        Integer customerId = 1;
        Integer productId = 4;
        Product product = entityManager.find(Product.class, productId);

        Customer customer = entityManager.find(Customer.class, customerId);
        CartItem item = repository.findByCustomerAndProduct(customer, product);

        assertThat(item).isNotNull();
        System.out.println(item);
    }


    @Test
    public void testDeleteByCustomerNProduct() {
        Integer customerId = 1;
        Integer productId = 4;
        Product product = entityManager.find(Product.class, productId);

        Customer customer = entityManager.find(Customer.class, customerId);
        repository.deleteByCustomerIdAndProductId(customerId, productId);
        Optional<CartItem> customerById = repository.findById(5);
        assertThat(!customerById.isPresent());
    }

    @Test
    public void testUpdateQuantity() {
        Integer customerId = 10;
        Integer productId = 10;
        Integer itemId = 2;
        int quantity = 1;

        repository.updateQuantity(quantity, customerId, productId);
        CartItem item = repository.findById(itemId).get();
        assertThat(item.getQuantity()).isEqualTo(quantity);
    }

}
