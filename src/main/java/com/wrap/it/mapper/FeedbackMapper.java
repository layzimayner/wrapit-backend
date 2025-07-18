package com.wrap.it.mapper;

import com.wrap.it.config.MapperConfig;
import com.wrap.it.dto.feedback.FeedbackDto;
import com.wrap.it.dto.feedback.FeedbackRequest;
import com.wrap.it.model.Feedback;
import com.wrap.it.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface FeedbackMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "message", source = "request.message")
    Feedback toModel(FeedbackRequest request, User user);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "feedbackId", source = "id")
    FeedbackDto toDto(Feedback feedback);
}
