package com.shopme.common.entity.product;


import com.shopme.common.Constants;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.IdBasedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor

public class Product extends IdBasedEntity {

    @Column(unique = true, length = 255, nullable = false)
    private String name;
    @Column(unique = true, length = 255, nullable = false)
    private String alias;
    @Column(length = 512, nullable = false, name = "short_description")
    private String shortDescription;
    @Column(length = 4096, nullable = false, name = "full_description")
    private String fullDescription;
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean enabled;
    @Column(name = "in_stock")
    private boolean inStock;

    private float cost;
    private float price;
    @Column(name = "discount_percent")
    private float discountPercent;

    @Column(name = "main_Image", nullable = false)
    private String mainImage;


    private float length;
    private float width;
    private float height;
    private float weight;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();
    // orphanRemoval löscht alle kinder die keine reference auf ihren Parent haben und cascade.all update die kindern, wenn sich was bei den Eltern ändert
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();


    private float averageRating;

    private int reviewCount;



    public Product(Integer id) {
        this.id = id;
    }

    public Product(String productName) {
        this.name = productName;
    }


    public void addExtraImage(String imageName) {
        this.images.add(new ProductImage(imageName, this));

    }



    public void addDetail(ProductDetail detail) {
        details.add(detail);
    }

    public void addDetail(String detailName, String detailValue) {
        details.add(new ProductDetail(detailName, detailValue, this));
    }

    public void addDetail(Integer id, String detailName, String detailValue) {
        details.add(new ProductDetail(id, detailName, detailValue, this));
    }


    @Transient
    public String getMainImagePath() {
        if (id == null || mainImage == null) return "/images/image-thumbnail.png";
        return Constants.S3_BASE_URI + "/product-images/" + this.id + "/" + this.mainImage;

    }


    @Override
    public String toString() {
        return "Product[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ']';
    }

    public boolean containsImageName(String imageName) {
        var iterator = images.iterator();
        while (iterator.hasNext()) {
            var image = iterator.next();
            if (image.getName().equals(imageName))
                return true;
        }
        return false;
    }

    @Transient
    public String getShortName() {
        if (name.length() > 70) {
            return name.substring(0, 70).concat("...");
        }
        return name;
    }

    @Transient
    public float getDiscountPrice() {
        if (discountPercent > 0) {
            return price * ((100 - discountPercent) / 100);
        }
        return price;
    }
}
