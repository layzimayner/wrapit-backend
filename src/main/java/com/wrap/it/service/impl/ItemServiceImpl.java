package com.wrap.it.service.impl;

import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemDtoWithoutCategoryIds;
import com.wrap.it.dto.item.ItemRequest;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.mapper.ItemMapper;
import com.wrap.it.model.Item;
import com.wrap.it.repository.ItemRepository;
import com.wrap.it.service.ImageService;
import com.wrap.it.service.ItemService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<ItemDtoWithoutCategoryIds> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(itemMapper::toDtoWithoutCategories);
    }

    @Override
    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't delete item with id "
                    + id + " because it does not exist");
        }
        itemRepository.deleteById(id);
    }

    @Override
    public ItemDto update(Long id, ItemRequest requestDto) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't update item with id "
                        + id + " because it does not exist")
        );
        itemMapper.update(item, requestDto);
        itemRepository.save(item);

        Set<ImageDto> imagesDto = imageService.save(requestDto.getImageUrls(), item);

        return imagesDto == null ? itemMapper.toDto(item) : itemMapper.toDtoWithImages(
                item, imagesDto);
    }

    @Override
    public ItemDto findById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find item with id "
                        + id + " because it does not exist")
        );
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDtoWithoutCategoryIds> getItemsByCategoryIds(Set<Long> categoryIds,
                                                                 Pageable pageable) {
        return List.of();
    }
}
