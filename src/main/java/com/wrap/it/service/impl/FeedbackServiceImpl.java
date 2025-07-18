package com.wrap.it.service.impl;

import com.wrap.it.dto.feedback.FeedbackDto;
import com.wrap.it.dto.feedback.FeedbackRequest;
import com.wrap.it.mapper.FeedbackMapper;
import com.wrap.it.model.Feedback;
import com.wrap.it.model.User;
import com.wrap.it.repository.FeedbackRepository;
import com.wrap.it.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public FeedbackDto save(FeedbackRequest request, User user) {
        Feedback model = feedbackMapper.toModel(request, user);
        return feedbackMapper.toDto(feedbackRepository.save(model));
    }

    @Override
    public Page<FeedbackDto> getAll(Pageable pageable) {
        return feedbackRepository.findAll(pageable)
                .map(feedbackMapper::toDto);
    }
}
