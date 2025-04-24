package com.mtechproject.gsastry.authenticationservice;

import com.mtechproject.gsastry.authenticationservice.domain.Role;
import com.mtechproject.gsastry.authenticationservice.domain.RoleEnum;
import com.mtechproject.gsastry.authenticationservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AuthenticationserviceApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationserviceApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		// initial role - data
		for(RoleEnum role : RoleEnum.values()) {
			roleRepository.save(new Role(role.toString()));
		}
	}
}
