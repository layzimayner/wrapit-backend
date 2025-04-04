package com.wrap.it.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class ItemRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String mainImageUrl;

    private Set<String> imageUrls;

    @NotEmpty
    private Set<Long> categoriesIds;

    @Positive
    private int quantity;
}
