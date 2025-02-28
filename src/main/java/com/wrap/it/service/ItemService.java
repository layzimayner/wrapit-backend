package com.wrap.it.service;

import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemRequest;

public interface ItemService {
    ItemDto save(ItemRequest request);
}
