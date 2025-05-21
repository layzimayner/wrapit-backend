package com.wrap.it.repository;

import com.wrap.it.model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM categories WHERE name = :name", nativeQuery = true)
    Optional<Category> findByNameIncludingDeleted(@Param("name") String name);
}
