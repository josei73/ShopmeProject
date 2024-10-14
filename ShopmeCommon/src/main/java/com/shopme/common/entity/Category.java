package com.shopme.common.entity;


import com.shopme.common.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends IdBasedEntity {

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @Transient
    private boolean hasChildren;

    @Column(name = "all_parent_ids", length = 256, nullable = true)
    private String allParentIDs;


    //Vater
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc ")
    private Set<Category> children = new HashSet<>();

    public Category(String name, String alias, String image) {
        this.name = name;
        this.alias = alias;
        this.image = image;
    }

    public Category(String name, Category parent) {
        this.parent = parent;
        this.name = name;
        this.alias = name;
        this.image = "default.png";


    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public Category(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.image = category.getImage();
        this.alias = category.getAlias();
        this.enabled = category.isEnabled();
        this.hasChildren = (category.getChildren().size() > 0);

    }

    public Category(String categoryName) {
        this.name = categoryName;
    }
    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }


    @Transient
    public String getImagePath() {
        if (id == null) return "/images/image-thumbnail.png";
        return Constants.S3_BASE_URI + "/categories-images/" + this.id + "/" + this.image;

    }

    public String getAllParentIDs() {
        return allParentIDs;
    }

    public void setAllParentIDs(String allParentIDs) {
        this.allParentIDs = allParentIDs;
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
