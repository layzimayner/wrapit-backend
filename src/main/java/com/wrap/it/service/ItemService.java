package com.wrap.it.service;

import com.wrap.it.dto.category.CategoryItemRequest;
import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemDtoWithReviews;
import com.wrap.it.dto.item.ItemRequest;
import com.wrap.it.dto.item.SlimItemDto;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;

public interface ItemService {
    ItemDto save(ItemRequest request);

    SlimItemDto delete(Long id);

    ItemDto update(Long id, ItemRequest requestDto);

    ItemDtoWithReviews findById(Long id);

    Page<SlimItemDto> findAll(CategoryItemRequest request,
                              BigDecimal minPrice,
                              BigDecimal maxPrice);
}
