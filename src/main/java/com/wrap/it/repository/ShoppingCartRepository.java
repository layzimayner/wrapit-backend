package com.wrap.it.repository;

import com.wrap.it.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT s FROM ShoppingCart s LEFT JOIN FETCH s.cartItems c WHERE s.id = :userId")
    Optional<ShoppingCart> getCartByUserId(@Param("userId") Long userId);

    @EntityGraph(value = "ShoppingCart.withOrderItemsAndBooks",
            type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.user.id = :userId")
    Optional<ShoppingCart> getFullInnitOfCartByUserId(@Param("userId") Long userId);
}
