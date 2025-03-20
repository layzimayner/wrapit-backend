package com.wrap.it.service;

import com.wrap.it.dto.review.ReviewDto;
import com.wrap.it.dto.review.ReviewRequest;
import com.wrap.it.dto.review.UpdateReviewRequest;
import com.wrap.it.model.User;

public interface ReviewService {
    ReviewDto postReview(User user, ReviewRequest request);

    void deleteReview(User user, Long reviewId);

    ReviewDto updateReview(User user, UpdateReviewRequest request);
}
