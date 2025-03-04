package com.wrap.it.service;

import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemDtoWithoutCategoryIds;
import com.wrap.it.dto.item.ItemRequest;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {
    ItemDto save(ItemRequest request);

    Page<ItemDtoWithoutCategoryIds> findAll(Pageable pageable);

    void delete(Long id);

    ItemDto update(Long id, ItemRequest requestDto);

    ItemDto findById(Long id);

    List<ItemDtoWithoutCategoryIds> getItemsByCategoryIds(Set<Long> categoryIds,
                                                          Pageable pageable);
}
