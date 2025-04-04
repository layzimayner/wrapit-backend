package com.wrap.it.repository;

import com.wrap.it.model.PasswordResetToken;
import com.wrap.it.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByCode(String code);

    void deleteByUser(User user);

    Optional<PasswordResetToken> findByCodeAndUser(String code, User user);
}
