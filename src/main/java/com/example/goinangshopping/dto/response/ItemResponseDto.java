package com.example.goinangshopping.dto.response;

import com.example.goinangshopping.model.ItemCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemResponseDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer discount;
    private String imageUrl;
    private ItemCategory category;
    private boolean available;
}
