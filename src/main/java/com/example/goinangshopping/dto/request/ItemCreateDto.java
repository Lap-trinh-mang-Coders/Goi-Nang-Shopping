package com.example.goinangshopping.dto.request;

import com.example.goinangshopping.model.ItemCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemCreateDto {

    @NotBlank(message = "Tên món không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    private Integer discount;

    private String imageUrl;

    @NotNull(message = "Danh mục không được để trống")
    private ItemCategory category;

    private boolean available = true;
}
