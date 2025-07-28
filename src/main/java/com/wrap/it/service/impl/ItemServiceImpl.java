package com.wrap.it.service.impl;

import com.wrap.it.dto.category.CategoryItemRequest;
import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemDtoWithReviews;
import com.wrap.it.dto.item.ItemRequest;
import com.wrap.it.dto.item.SlimItemDto;
import com.wrap.it.dto.review.ReviewDto;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.exception.SortBuildingException;
import com.wrap.it.exception.TakenNameException;
import com.wrap.it.mapper.ItemMapper;
import com.wrap.it.mapper.ReviewMapper;
import com.wrap.it.model.Item;
import com.wrap.it.repository.ItemRepository;
import com.wrap.it.repository.ReviewRepository;
import com.wrap.it.service.ImageService;
import com.wrap.it.service.ItemService;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final Long INIT_VERSION = 1L;

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final ImageService imageService;
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public ItemDto save(ItemRequest request) {
        Optional<Item> optionalFromDb = itemRepository
                .findByNameIncludingDeleted(request.getName());

        if (optionalFromDb.isPresent()) {
            Item itemFromDb = optionalFromDb.get();

            if (itemFromDb.isDeleted()) {
                itemMapper.update(itemFromDb, request);
                itemFromDb.setDeleted(false);
                return itemMapper.toDto(itemRepository.save(itemFromDb));
            }
            throw new TakenNameException("Item with name " + request.getName()
                    + " already exist");
        }

        Item item = itemMapper.toModel(request);
        item.setVersion(INIT_VERSION);

        item = itemRepository.save(item);

        Set<ImageDto> imagesDto = imageService.save(request.getImageUrls(), item);

        return imagesDto == null ? itemMapper.toDto(item) : itemMapper.toInitDto(
                item, imagesDto);
    }

    @Override
    public Page<SlimItemDto> findAll(CategoryItemRequest request) {
        if (request.categoryIds() == null || request.categoryIds().isEmpty()) {
            return itemRepository.findAll(toPageable(request))
                    .map(itemMapper::toSlimDto);
        } else {
            return getItemsByCategoryIds(request);
        }
    }

    @Override
    public SlimItemDto delete(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't delete item with id "
                        + id + " because it does not exist")
        );
        itemRepository.deleteById(id);
        return itemMapper.toSlimDto(item);
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

        return imagesDto == null ? itemMapper.toDto(item) : itemMapper.toInitDto(
                item, imagesDto);
    }

    @Override
    public ItemDtoWithReviews findById(Long id) {
        Item item = itemRepository.findByIdWithCategories(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find item with id "
                        + id + " because it does not exist")
        );

        List<ReviewDto> reviews = reviewRepository.findAllByItemId(id).stream()
                .map(reviewMapper::toDto)
                .toList();

        return itemMapper.toDtoWithReviews(item, reviews);
    }

    private Page<SlimItemDto> getItemsByCategoryIds(CategoryItemRequest request) {
        Pageable pageable = toPageable(request);

        return itemRepository.findByCategoryIdIn(request.categoryIds(),
                        request.categoryIds().size(), pageable)
                .map(itemMapper::toSlimDto);
    }

    private Pageable toPageable(CategoryItemRequest request) {
        if (request.sort() == null || request.sort().length == 0) {
            return PageRequest.of(request.page(), request.size());
        }

        Sort.Order[] orders = Arrays.stream(request.sort())
                .map(this::parseSortOrder)
                .toArray(Sort.Order[]::new);

        return PageRequest.of(request.page(), request.size(), Sort.by(orders));
    }

    private Sort.Order parseSortOrder(String sortString) {
        String[] parts = sortString.split(",");
        if (parts.length != 2) {
            throw new SortBuildingException("Invalid sort format. Expected: 'field,direction'");
        }

        String field = parts[0].trim();
        String direction = parts[1].trim().toLowerCase();

        return switch (direction) {
            case "asc" -> Sort.Order.asc(field);
            case "desc" -> Sort.Order.desc(field);
            default -> throw new SortBuildingException(
                    "Invalid sort direction. Use 'asc' or 'desc'");
        };
    }
}
