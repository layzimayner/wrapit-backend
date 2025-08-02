package com.wrap.it.repository;

import com.wrap.it.model.Item;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.id = :categoryId")
    List<Item> findAllByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT i FROM Item i JOIN FETCH i.categories c WHERE i.id = :itemId")
    Optional<Item> findByIdWithCategories(@Param("itemId") Long itemId);

    @Query("""
            SELECT i FROM Item i
            JOIN i.categories c
            WHERE c.id IN :categoryIds
              AND i.price >= :minPrice
              AND i.price <= :maxPrice
            GROUP BY i
            HAVING COUNT(DISTINCT c.id) = :categoryCount
            """)
    Page<Item> findByCategoryIdIn(Set<Long> categoryIds,
                                  long categoryCount,
                                  Pageable pageable,
                                  BigDecimal minPrice,
                                  BigDecimal maxPrice);

    @Query("SELECT i FROM Item i WHERE i.price >= :minPrice AND i.price <= :maxPrice")
    Page<Item> findAll(Pageable pageable, BigDecimal minPrice, BigDecimal maxPrice);

    @Query(value = "SELECT * FROM items WHERE name = :name", nativeQuery = true)
    Optional<Item> findByNameIncludingDeleted(@Param("name") String name);
}
