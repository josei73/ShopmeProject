package com.shopme.common.entity;


import com.shopme.common.entity.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review extends IdBasedEntity {

    @Column(length = 128, nullable = false)
    private String headline;
    @Column(length = 300,nullable = false)
    private String comment;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private Date reviewTime;


    @Override
    public String toString() {
        return "Review{" +
                "headline='" + headline + '\'' +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                ", product=" + product.getName() +
                ", customer=" + customer.getFullName() +
                ", reviewTime=" + reviewTime +
                '}';
    }
}
