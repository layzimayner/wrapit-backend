package com.wrap.it.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Table(name = "reviews")
@SQLDelete(sql = "UPDATE reviews SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted = false")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, precision = 10, scale = 1)
    private BigDecimal rating;

    private String comment;

    @Column(nullable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
