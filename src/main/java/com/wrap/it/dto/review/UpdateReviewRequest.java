package com.wrap.it.dto.review;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateReviewRequest(
        @Positive Long reviewId,
        @Size(min = 5, max = 500, message =
                "Comment must be between 5 and 500 characters")String comment,
        double rating) {
}
