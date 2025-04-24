package com.mtechproject.gsastry.authenticationservice.repository;

import com.mtechproject.gsastry.authenticationservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);  // You can query by roleName
}
