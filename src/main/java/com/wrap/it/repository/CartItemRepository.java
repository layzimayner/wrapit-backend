package com.wrap.it.repository;

import com.wrap.it.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci "
            + "JOIN FETCH ci.item i "
            + "JOIN ci.shoppingCart sc "
            + "WHERE ci.id = :ItemId AND sc.id = :shoppingCartId")
    Optional<CartItem> findByIdAndShoppingCartId(Long itemId, Long shoppingCartId);
}
