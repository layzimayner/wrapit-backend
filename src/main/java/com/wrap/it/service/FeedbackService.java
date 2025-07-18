package com.wrap.it.service;

import com.wrap.it.dto.feedback.FeedbackDto;
import com.wrap.it.dto.feedback.FeedbackRequest;
import com.wrap.it.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {

    FeedbackDto save(FeedbackRequest feedback, User user);

    Page<FeedbackDto> getAll(Pageable pageable);
}
