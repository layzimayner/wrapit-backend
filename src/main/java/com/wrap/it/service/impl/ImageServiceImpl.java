package com.wrap.it.service.impl;

import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.mapper.ImageMapper;
import com.wrap.it.model.Image;
import com.wrap.it.model.Item;
import com.wrap.it.repository.ImageRepository;
import com.wrap.it.service.ImageService;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;

    @Override
    public Set<ImageDto> save(Set<String> imageUrls, Item item) {
        if (imageUrls == null) {
            return Set.of(new ImageDto());
        }

        Set<Image> images = imageUrls.stream()
                .map(u -> imageMapper.toModel(u, item))
                .collect(Collectors.toSet());

        imageRepository.saveAll(images);

        return images.stream()
                .map(imageMapper::toDtoWithoutItemId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ImageDto> getImagesById(Long id) {
        return imageRepository.findAllById(id).stream()
                .map(imageMapper::toDtoWithoutItemId)
                .collect(Collectors.toSet());
    }
}
