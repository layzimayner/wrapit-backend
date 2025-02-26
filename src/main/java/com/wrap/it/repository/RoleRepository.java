package com.wrap.it.repository;

import com.wrap.it.model.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(Role.RoleName roleName);

    @Query("SELECT r FROM Role r WHERE r.id IN :ids")
    Set<Role> findRolesByIds(@Param("ids") Set<Long> ids);
}
