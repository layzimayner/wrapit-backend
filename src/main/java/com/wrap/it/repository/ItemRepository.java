package com.wrap.it.repository;

import com.wrap.it.model.Item;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.id = :categoryId")
    List<Item> findAllByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT i FROM Item i JOIN FETCH i.categories c WHERE i.id = :itemId")
    Optional<Item> findByIdWithCategories(@Param("itemId") Long itemId);

    @Query("""
    SELECT i FROM Item i
    JOIN i.categories c
    WHERE c.id IN :categoryIds
    GROUP BY i
    HAVING COUNT(DISTINCT c.id) = :categoryCount
    """)
    Page<Item> findByCategoryIdIn(@Param("categoryIds") Set<Long> categoryIds,
                                  @Param("categoryCount") long categoryCount,
                                  Pageable pageable);
}
