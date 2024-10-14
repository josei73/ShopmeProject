package com.shopme.common.entity;

import com.shopme.common.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
public class Brand extends IdBasedEntity {

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 128, nullable = false)
    private String logo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Brand(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;

    }


    public void addCategory(Category category) {
        this.categories.add(category);
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", categories=" + categories +
                '}';
    }

    @Transient
    public String getLogoPath() {
        if (id == null) return "/images/image-thumbnail.png";
        return Constants.S3_BASE_URI + "/brand-logos/" + this.id + "/" + this.logo;

    }
}
