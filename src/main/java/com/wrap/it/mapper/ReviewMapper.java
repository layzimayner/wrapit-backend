package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.review.ReviewDto;
import com.wrap.it.dto.review.ReviewRequest;
import com.wrap.it.dto.review.UpdateReviewRequest;
import com.wrap.it.model.Item;
import com.wrap.it.model.Review;
import com.wrap.it.model.User;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Review toModel(Item item,
                   User user,
                   ReviewRequest request,
                   LocalDateTime createdAt);

    @Mapping(target = "userName", source = "user")
    ReviewDto toDto(Review review);

    default String mapUserToUserName(User user) {
        return user != null ? user.getFullName() : null;
    }

    void update(UpdateReviewRequest request, @MappingTarget Review review, LocalDateTime newDate);
}
