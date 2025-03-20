package com.wrap.it.dto.item;

import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.dto.review.ReviewDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class ItemDtoWithReviews {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String mainImageUrl;
    private Set<ImageDto> images;
    private Set<Long> categoriesIds;
    private int totalReviews;
    private double averageRating;
    private List<ReviewDto> reviews;
}
