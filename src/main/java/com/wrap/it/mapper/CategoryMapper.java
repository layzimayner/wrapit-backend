package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.category.CategoryDto;
import com.wrap.it.dto.category.CreateCategoryRequestDto;
import com.wrap.it.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto requestDto);

    void updateCategoryFromDto(CreateCategoryRequestDto requestDto,
                               @MappingTarget Category category);
}
