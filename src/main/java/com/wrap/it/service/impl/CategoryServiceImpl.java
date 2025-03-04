package com.wrap.it.service.impl;

import com.wrap.it.dto.category.CategoryDto;
import com.wrap.it.dto.category.CreateCategoryRequestDto;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.mapper.CategoryMapper;
import com.wrap.it.model.Category;
import com.wrap.it.repository.CategoryRepository;
import com.wrap.it.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find category by id " + id)
        );
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toEntity(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't update category with id "
                        + id + " because it does not exist")
        );
        categoryMapper.updateCategoryFromDto(requestDto, category);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't delete category with id "
                    + id + " because it does not exist");
        }
        categoryRepository.deleteById(id);
    }
}
