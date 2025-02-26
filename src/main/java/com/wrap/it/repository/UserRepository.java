package com.wrap.it.repository;

import com.wrap.it.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.phoneNumber = :phoneNumber")
    Optional<User> findByEmailWithRoles(String email);

    boolean existsByEmail(String email);
}
