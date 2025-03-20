package com.wrap.it.service.impl;

import com.wrap.it.dto.review.ReviewDto;
import com.wrap.it.dto.review.ReviewRequest;
import com.wrap.it.dto.review.UpdateReviewRequest;
import com.wrap.it.exception.AccessDeniedException;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.mapper.ReviewMapper;
import com.wrap.it.model.Item;
import com.wrap.it.model.Review;
import com.wrap.it.model.Role;
import com.wrap.it.model.User;
import com.wrap.it.repository.ItemRepository;
import com.wrap.it.repository.ReviewRepository;
import com.wrap.it.repository.RoleRepository;
import com.wrap.it.service.ReviewService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public ReviewDto postReview(User user, ReviewRequest request) {
        Item item = itemRepository.findById(request.itemId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find item with id "
                        + request.itemId() + " because it does not exist")
        );

        calculateNewAverage(request.rating(), item);

        LocalDateTime createdAt = LocalDateTime.now();
        Review review = reviewMapper.toModel(item,user,request, createdAt);
        reviewRepository.save(review);

        return reviewMapper.toDto(review);
    }

    @Override
    public void deleteReview(User user, Long reviewId) {
        Review review = reviewRepository.findByIdWithUserAndItem(reviewId).orElseThrow(() ->
                new EntityNotFoundException("Can't find review with id "
                        + reviewId + " because it does not exist")
        );

        Role role = roleRepository.findByName(Role.RoleName.ADMIN);

        if (!review.getUser().equals(user) || user.getAuthorities().contains(role)) {
            calculateOldAverage(review.getRating().doubleValue(), review.getItem());
            reviewRepository.deleteById(reviewId);
        } else {
            throw new AccessDeniedException("You don't have permission to delete this review");
        }
    }

    @Override
    @Transactional
    public ReviewDto updateReview(User user, UpdateReviewRequest request) {
        Review review = reviewRepository.findByIdWithUserAndItem(
                request.reviewId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find review with id "
                        + request.reviewId() + " because it does not exist")
        );

        Role role = roleRepository.findByName(Role.RoleName.ADMIN);

        if (!review.getUser().equals(user) || user.getAuthorities().contains(role)) {
            calculateOldAverage(review.getRating().doubleValue(), review.getItem());
            calculateNewAverage(request.rating(), review.getItem());

            LocalDateTime newDate = LocalDateTime.now();
            reviewMapper.update(request, review, newDate);
            return reviewMapper.toDto(reviewRepository.save(review));
        } else {
            throw new AccessDeniedException("You don't have permission to delete this review");
        }
    }

    private void calculateNewAverage(double rating, Item item) {
        double newAverageRating = (item.getAverageRating() * item.getTotalReviews()
                + rating) / (item.getTotalReviews() + 1);
        item.setTotalReviews(item.getTotalReviews() + 1);
        item.setAverageRating(newAverageRating);

        itemRepository.save(item);
    }

    private void calculateOldAverage(double rating, Item item) {
        double oldAverageRating = (item.getAverageRating() * item.getTotalReviews()
                - rating) / (item.getTotalReviews() - 1);
        item.setTotalReviews(item.getTotalReviews() - 1);
        item.setAverageRating(oldAverageRating);

        itemRepository.save(item);
    }
}
