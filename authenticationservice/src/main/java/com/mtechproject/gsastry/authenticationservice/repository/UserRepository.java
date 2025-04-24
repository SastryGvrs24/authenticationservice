package com.mtechproject.gsastry.authenticationservice.repository;

import com.mtechproject.gsastry.authenticationservice.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);

}
