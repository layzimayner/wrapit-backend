package com.wrap.it.dto.review;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReviewDto {
    private String userName;
    private Long reviewId;
    private double rating;
    private String comment;
    private LocalDateTime createdAt;
}
