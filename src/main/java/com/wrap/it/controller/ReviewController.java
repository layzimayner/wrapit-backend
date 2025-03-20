package com.wrap.it.controller;

import com.wrap.it.dto.review.ReviewDto;
import com.wrap.it.dto.review.ReviewRequest;
import com.wrap.it.dto.review.UpdateReviewRequest;
import com.wrap.it.model.User;
import com.wrap.it.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "review management",
        description = "Endpoints for management reviews")
@RestController
@RequestMapping("/reviews")
@Validated
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Post review", description = "Add new review to DB")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto postReview(Authentication authentication,
                                @RequestBody @Valid ReviewRequest request) {
        User user = (User)authentication.getPrincipal();
        return reviewService.postReview(user, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review", description = "Delete review from DB")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(Authentication authentication, @Positive @PathVariable Long reviewId) {
        User user = (User)authentication.getPrincipal();
        reviewService.deleteReview(user, reviewId);
    }

    @PutMapping
    @Operation(summary = "Update review", description = "Update review from DB")
    public ReviewDto updateReview(Authentication authentication,
                             @Valid @RequestBody UpdateReviewRequest request) {
        User user = (User)authentication.getPrincipal();
        return reviewService.updateReview(user, request);
    }
}
