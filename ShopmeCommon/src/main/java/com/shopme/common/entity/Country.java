package com.shopme.common.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
public class Country extends IdBasedEntity  {

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 5, nullable = false)
    private String code;

    @OneToMany(mappedBy = "country")
    private Set<State> states = new HashSet<>();

    public Country( String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Country(Integer id) {
        this.id=id;
    }
    public void addState(String name){
        states.add(new State(name,this));
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
