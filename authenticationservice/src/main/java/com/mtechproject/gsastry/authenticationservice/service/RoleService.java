package com.mtechproject.gsastry.authenticationservice.service;

import com.mtechproject.gsastry.authenticationservice.domain.Role;
import com.mtechproject.gsastry.authenticationservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleService {

    @Autowired
    private RoleRepository rolesRepository;

    public Role getRoleByName(String roleName) {
        return rolesRepository.findByRoleName(roleName); // Assuming Roles repository exists
    }
}
