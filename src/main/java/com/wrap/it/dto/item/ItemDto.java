package com.wrap.it.dto.item;

import com.wrap.it.dto.image.ImageDto;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String mainImageUrl;
    private Set<ImageDto> images;
    private Set<Long> categoriesIds;
    private int totalReviews;
    private double averageRating;
    private short quantity;
    private Long version;
}
