package com.ideas2it.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductDtos {
    public static class CreateRequest {
        @NotBlank public String name;
        public String description;
        @NotNull @Min(0) public BigDecimal price;
        @NotNull @Min(0) public Integer stock;
    }

    public static class ProductResponse {
        public Integer productId;
        public String name;
        public String description;
        public BigDecimal price;
        public Integer stock;
    }
}


