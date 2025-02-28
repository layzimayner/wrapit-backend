package com.wrap.it.service.impl;

import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemRequest;
import com.wrap.it.mapper.ItemMapper;
import com.wrap.it.model.Item;
import com.wrap.it.repository.ItemRepository;
import com.wrap.it.service.ImageService;
import com.wrap.it.service.ItemService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final ImageService imageService;

    @Override
    @Transactional
    public ItemDto save(ItemRequest request) {
        Item item = itemRepository.save(itemMapper.toModel(request));

        Set<ImageDto> imagesDto = imageService.save(request.getImageUrls(), item);

        return imagesDto == null ? itemMapper.toDto(item) : itemMapper.toDtoWithImages(
                item, imagesDto);
    }
}
