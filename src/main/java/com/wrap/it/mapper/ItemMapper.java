package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemDtoWithReviews;
import com.wrap.it.dto.item.ItemRequest;
import com.wrap.it.dto.item.SlimItemDto;
import com.wrap.it.dto.review.ReviewDto;
import com.wrap.it.model.Category;
import com.wrap.it.model.Item;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface ItemMapper {
    Item toModel(ItemRequest request);

    @Mapping(target = "images", source = "imagesDto")
    ItemDto toInitDto(Item item, Set<ImageDto> imagesDto);

    ItemDto toDto(Item item);

    SlimItemDto toSlimDto(Item item);

    @Mapping(
            target = "categoriesIds",
            source = "item.categories",
            qualifiedByName = "mapCategoriesToIds"
    )
    ItemDtoWithReviews toDtoWithReviews(Item item, List<ReviewDto> reviews);

    void update(@MappingTarget Item item, ItemRequest requestDto);

    @AfterMapping
    default void setCategory(ItemRequest requestDto, @MappingTarget Item item) {
        Set<Category> categories = requestDto.getCategoriesIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        item.setCategories(categories);
    }

    @AfterMapping
    default void setCategoriesIds(@MappingTarget ItemDto itemDto, Item item) {
        Set<Long> categoriesIds = item.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        itemDto.setCategoriesIds(categoriesIds);
    }

    @Named("mapCategoriesToIds")
    default Set<Long> mapCategoriesToIds(Set<Category> categories) {
        if (categories == null) {
            return Set.of();
        }
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }
}
