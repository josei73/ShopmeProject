package com.shopme.common.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "shipping_rates")
@Getter
@Setter
@NoArgsConstructor
public class ShippingRate extends IdBasedEntity {
    private float rate;
    private int days;

    @Column(name = "cod_supported")
    private boolean codSupported;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false, length = 45)
    private String state;


    @Override
    public String toString() {
        return "ShippingRate{" +
                "id=" + id +
                ", rate=" + rate +
                ", days=" + days +
                ", codSupported=" + codSupported +
                ", country=" + country.getName() +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShippingRate that = (ShippingRate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
