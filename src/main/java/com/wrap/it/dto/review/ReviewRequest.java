package com.wrap.it.dto.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequest(
        double rating,
        @Size(min = 5, max = 500, message =
                "Comment must be between 5 and 500 characters") String comment,
        @NotNull Long itemId) {
}
