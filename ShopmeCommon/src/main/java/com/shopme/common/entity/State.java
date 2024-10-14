package com.shopme.common.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "states")
@Getter
@Setter
@NoArgsConstructor
public class State extends IdBasedEntity {

    @Column(length = 45, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;


    public State(String name) {
        this.name = name;
    }



    public State(String name, Country country) {
        this.name=name;
        this.country=country;
    }

}
