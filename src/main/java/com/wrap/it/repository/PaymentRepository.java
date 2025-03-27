package com.wrap.it.repository;

import com.wrap.it.model.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatus(Payment.Status status);

    Optional<Payment> findBySessionId(String sessionId);

    @Query("SELECT p FROM Payment p JOIN p.order o JOIN o.user u WHERE u.id = :userId")
    Page<Payment> findAll(Pageable pageable, Long userId);
}
