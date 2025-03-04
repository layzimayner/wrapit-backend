package com.wrap.it.dto.item;

import com.wrap.it.dto.image.ImageDto;
import java.math.BigDecimal;
import java.util.Set;

public class ItemDtoWithoutCategoryIds {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String mainImageUrl;
    private Set<ImageDto> images;
}
