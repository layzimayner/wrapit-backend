package com.wrap.it.dto.item;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SlimItemDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String mainImageUrl;
    private int totalReviews;
    private double averageRating;
    private int quantity;
    private String description;
    private Long version;
}
