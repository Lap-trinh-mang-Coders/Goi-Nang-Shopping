package com.example.goinangshopping.dto.request;

import com.example.goinangshopping.model.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer discount;
    private String imageUrl;
    private ItemCategory category;
    private boolean available;
}
