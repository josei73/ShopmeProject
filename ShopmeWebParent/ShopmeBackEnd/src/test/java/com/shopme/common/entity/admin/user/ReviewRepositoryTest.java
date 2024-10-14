package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.admin.user.user.repository.ReviewRepository;
import com.shopme.common.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)

public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void testCreateReview() {
        Integer productId = 4;
        Product product = new Product(productId);
        Integer customerId = 10;
        Customer customer = entityManager.find(Customer.class, customerId);


        Review review = new Review();
        review.setReviewTime(new Date());
        review.setCustomer(customer);
        review.setProduct(product);
        review.setHeadline("Perfect for my  it");
        review.setComment("Nice to have: wireless r");

        review.setRating(1);
        Review savedReview = reviewRepository.save(review);

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getId()).isGreaterThan(0);


    }

    @Test
    public void testListReviews(){
        List<Review> all = reviewRepository.findAll();
        all.forEach(System.out::println);
    }



}
