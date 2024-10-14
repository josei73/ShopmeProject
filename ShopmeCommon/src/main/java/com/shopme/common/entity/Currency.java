package com.shopme.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "currencies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency extends IdBasedEntity {

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 3, nullable = false)
    private String symbol;

    @Column(length = 4, nullable = false)
    private String code;




    public Currency(Integer id, String name, String symbol, String code) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.code = code;
    }




    @Override
    public String toString() {
        return name +" - "+code+" - "+symbol;
    }
}
