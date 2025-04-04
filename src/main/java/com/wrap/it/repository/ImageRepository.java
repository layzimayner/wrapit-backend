package com.wrap.it.repository;

import com.wrap.it.model.Image;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i WHERE i.item.id = :itemId")
    Set<Image> findAllById(@Param("itemId") Long itemId);
}
