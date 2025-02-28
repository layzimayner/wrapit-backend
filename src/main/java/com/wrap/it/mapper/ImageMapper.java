package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.model.Image;
import com.wrap.it.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ImageMapper {
    @Mapping(target = "item", source = "item")
    @Mapping(target = "id", ignore = true)
    Image toModel(String imageUrl, Item item);

    ImageDto toDtoWithoutItemId(Image image);
}
