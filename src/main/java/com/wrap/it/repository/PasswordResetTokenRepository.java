package com.wrap.it.repository;

import com.wrap.it.model.PasswordResetToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByCode(String code);

    Optional<PasswordResetToken> findByCodeAndEmail(String code, String email);

    void deleteAllByEmail(String email);
}
