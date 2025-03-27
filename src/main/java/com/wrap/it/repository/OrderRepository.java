package com.wrap.it.repository;

import com.wrap.it.model.Order;
import com.wrap.it.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    Page<Order> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT i FROM OrderItem i WHERE i.order.id IN :orderIds")
    List<OrderItem> findItemsByOrderIds(@Param("orderIds") List<Long> orderIds);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

    Optional<Order> findFinishedById(Long orderId);
}
