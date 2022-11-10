package com.vti.testing.repository;

import com.vti.testing.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
