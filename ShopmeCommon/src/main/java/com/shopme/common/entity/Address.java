package com.shopme.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor

public class Address extends AbstractAddressWithCountry {

    @Column(name = "default_address")
    private boolean defaultForShipping;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    public Customer getCustomer() {
        return customer;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


}

