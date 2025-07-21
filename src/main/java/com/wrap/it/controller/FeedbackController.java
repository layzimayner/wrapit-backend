package com.wrap.it.controller;

import com.wrap.it.dto.feedback.FeedbackDto;
import com.wrap.it.dto.feedback.FeedbackRequest;
import com.wrap.it.model.User;
import com.wrap.it.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Feedback management", description = "Endpoints for management Feedback")
@RestController
@RequestMapping("/feedbacks")
@Validated
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Leave a feedback", description = "Add new feedback to DB")
    public FeedbackDto leaveFeedback(@RequestBody FeedbackRequest request,
                                     Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return feedbackService.save(request, user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all feedbacks", description = "Return page of unread feedbacks")
    public Page<FeedbackDto> getAllFeedbacks(Pageable pageable) {
        return feedbackService.getAll(pageable);
    }
}
