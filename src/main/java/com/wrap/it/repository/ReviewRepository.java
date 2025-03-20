package com.wrap.it.repository;

import com.wrap.it.model.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByItemId(Long id);

    @Query("SELECT r FROM Review r JOIN FETCH "
            + "r.user u JOIN FETCH r.item i WHERE r.id = :reviewId")
    Optional<Review> findByIdWithUserAndItem(@Param("reviewId") Long reviewId);
}
