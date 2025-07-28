package com.wrap.it.dto.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import java.util.Set;

public record CategoryItemRequest(Set<Long> categoryIds,
                                  @Positive(message = "Page size must be positive")
                                  @Max(value = 100, message = "Page size cannot exceed 100")
                                  int size,

                                  @Positive(message = "Page number must be positive or zero")
                                  int page,

                                  String[] sort
) {
}
