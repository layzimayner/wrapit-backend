package com.wrap.it.dto.item;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SlimItemDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String mainImageUrl;
    private int totalReviews;
    private double averageRating;
}
