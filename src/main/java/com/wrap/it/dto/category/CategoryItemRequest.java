package com.wrap.it.dto.category;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record CategoryItemRequest(@NotEmpty Set<Long> categoryIds) {
}
