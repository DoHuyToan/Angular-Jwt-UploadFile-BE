package com.example.angularjwtuploadfilebe.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

// unique: ko trùng lặp
@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String name;
    private LocalDate dateOfManufacture;
    private String avatarProduct;
    @Size(max = 200)
    private String description;
    @ManyToOne
    User user;

    public Product() {}

    public Product(Long id, String name, LocalDate dateOfManufacture, String avatarProduct, String description, User user) {
        this.id = id;
        this.name = name;
        this.dateOfManufacture = dateOfManufacture;
        this.avatarProduct = avatarProduct;
        this.description = description;
        this.user = user;
    }

    public Product(String name, LocalDate dateOfManufacture, String avatarProduct, String description, User user) {
        this.name = name;
        this.dateOfManufacture = dateOfManufacture;
        this.avatarProduct = avatarProduct;
        this.description = description;
        this.user = user;
    }

    public Product(String name, LocalDate dateOfManufacture, String avatarProduct, String description) {
        this.name = name;
        this.dateOfManufacture = dateOfManufacture;
        this.avatarProduct = avatarProduct;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(LocalDate dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    public String getAvatarProduct() {
        return avatarProduct;
    }

    public void setAvatarProduct(String avatarProduct) {
        this.avatarProduct = avatarProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
