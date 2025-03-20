package com.wrap.it.dto.category;

import java.util.Set;

public record CategoryItemRequest(Set<Long> categoryIds) {
}
