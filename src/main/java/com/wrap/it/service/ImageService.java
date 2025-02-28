package com.wrap.it.service;

import com.wrap.it.dto.image.ImageDto;
import com.wrap.it.model.Item;
import java.util.Set;

public interface ImageService {
    Set<ImageDto> save(Set<String> imageUrls, Item item);
}
