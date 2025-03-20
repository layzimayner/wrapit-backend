package com.wrap.it.dto.item;

import java.math.BigDecimal;
import lombok.Data;

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
