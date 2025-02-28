package com.wrap.it.dto.item;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Set<ItemDto> images;
}
