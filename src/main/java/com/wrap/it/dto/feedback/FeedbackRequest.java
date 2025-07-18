package com.wrap.it.dto.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FeedbackRequest(
        @NotBlank(message = "Feedback must not be blank")
        @Size(min = 10, max = 1000, message = "Feedback must be between 10 and 1000 characters")
        String message
) {
}
