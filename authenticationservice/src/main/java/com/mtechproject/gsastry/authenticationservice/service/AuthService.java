package com.mtechproject.gsastry.authenticationservice.service;

import com.mtechproject.gsastry.authenticationservice.domain.*;
import com.mtechproject.gsastry.authenticationservice.dto.LoginResponse;
import com.mtechproject.gsastry.authenticationservice.dto.SignUpRequest;
import com.mtechproject.gsastry.authenticationservice.repository.RoleRepository;
import com.mtechproject.gsastry.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTService jwtService;

	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;


	@Autowired
	private RoleRepository roleRepository;

	public LoginResponse authenticateUser(String username, String password) {
		// Authenticate using the authentication manager
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Fetch customer from the database by username
		AppUser appUser = userRepository.findByUsername(username);

		// Extract roles from the authenticated customer
		Role role = appUser.getRole();

		// Generate JWT token including roles
		String token = jwtService.generateToken(username, role);

		// Return LoginResponse with username, token, and roles
		return new LoginResponse(appUser.getUsername(), token, role.getRoleName());
	}

	public boolean isUsernameAvailable(String username) {
		AppUser isUserNameAlreadyTaken = userRepository.findByUsername(username);
		return isUserNameAlreadyTaken != null;
	}

	public boolean createUser(SignUpRequest signUpRequest) throws Exception {

		AppUser appUser;

		if(signUpRequest.isJobSeeker()) {
			appUser = new JobSeeker();
			appUser.setRole(roleRepository.findByRoleName(RoleEnum.JOB_SEEKER.toString()));
		} else {
			appUser = new Recruiter();
			appUser.setRole(roleRepository.findByRoleName(RoleEnum.RECRUITER.toString()));
		}

		appUser.setFullName(signUpRequest.getUserFullName());
		appUser.setUsername(signUpRequest.getUserName());

		// Encrypt password before saving
		appUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

		try {
			userRepository.save(appUser);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
