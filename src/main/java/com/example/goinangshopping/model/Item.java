package com.example.goinangshopping.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer discount;
    private String imageUrl;

    @Enumerated(EnumType.STRING) // Thêm annotation này
    private ItemCategory category;

    private boolean available;
}