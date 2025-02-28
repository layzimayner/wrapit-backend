package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemRequest;
import com.wrap.it.model.Item;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ItemMapper {
    Item toModel(ItemRequest request);

    @Mapping(target = "images", source = "imagesDto")
    ItemDto toDtoWithImages(Item item, Set<ImageDto> imagesDto);

    ItemDto toDto(Item item);
}
