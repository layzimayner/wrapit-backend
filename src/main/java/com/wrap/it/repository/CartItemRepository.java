package com.wrap.it.repository;

import com.wrap.it.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long itemId, Long shoppingCartId);
}
