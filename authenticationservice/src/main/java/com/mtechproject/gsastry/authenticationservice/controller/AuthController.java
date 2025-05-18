package com.mtechproject.gsastry.authenticationservice.controller;


import com.mtechproject.gsastry.authenticationservice.dto.*;
import com.mtechproject.gsastry.authenticationservice.service.AuthService;
import com.mtechproject.gsastry.authenticationservice.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    private String authHeader;

    // Register a new Customer
    @PostMapping("/signup")
    public ResponseEntity<Response<SignUpResponse>> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        Response<SignUpResponse> response = new Response<>();
        try {
            // Check if the username is already taken
            boolean usernameExists = authService.isUsernameAvailable(signUpRequest.getUserName());
            if (usernameExists) {
                response.setResponseCode(HttpStatus.BAD_REQUEST);
                response.setErrorMessage("Username is already taken. Please choose another one.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }


            // Save the admin
            boolean isUserCreated = authService.createUser(signUpRequest);
            SignUpResponse signUpResponse = new SignUpResponse();
            // Prepare the success message
            String successMessage = "User registered successfully";

            if(isUserCreated) {
                // Populate the response with success message and structured data
                signUpResponse.setUsername(signUpRequest.getUserName());
                response.setResponseCode(HttpStatus.CREATED);
                response.setData(signUpResponse); // Set the structured data as part of the response
                response.setMessage(successMessage);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                signUpResponse.setUsername(signUpRequest.getUserName());
                response.setResponseCode(HttpStatus.BAD_REQUEST);
                response.setData(signUpResponse); // Set the structured data as part of the response
                response.setMessage(successMessage);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }

        } catch (Exception e) {
            // Handle error case
            response.setResponseCode(HttpStatus.BAD_REQUEST);
            response.setErrorMessage("Sign-up failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user and generate token
            LoginResponse loginResponse = authService.authenticateUser(loginRequest.getUsername(),
                    loginRequest.getPassword());

            // Prepare the response with JWT token
            Response<LoginResponse> response = new Response<>();
            response.setResponseCode(HttpStatus.OK);
            response.setData(loginResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Handle any authentication failures
            Response<LoginResponse> errorResponse = new Response<>();
            errorResponse.setResponseCode(HttpStatus.UNAUTHORIZED);
            errorResponse.setErrorMessage("Authentication failed: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Response<TokenValidationResponse>> validateToken(@RequestParam("token") String authHeader) {
        this.authHeader = authHeader;
        Response<TokenValidationResponse> response = new Response<>();
        TokenValidationResponse tokenResponse = new TokenValidationResponse();

        try {
            if (authHeader == null) {
                throw new IllegalArgumentException("Invalid Authorization header format");
            }

            tokenResponse.setValid(jwtService.validateToken(authHeader));
            tokenResponse.setUsername(jwtService.extractUserName(authHeader));
            tokenResponse.setRoles(jwtService.extractRoles(authHeader));
            tokenResponse.setMessage("Token is valid");

            response.setResponseCode(HttpStatus.OK);
            response.setData(tokenResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            tokenResponse.setValid(false);
            tokenResponse.setMessage("Token validation failed: " + e.getMessage());

            response.setResponseCode(HttpStatus.UNAUTHORIZED);
            response.setData(tokenResponse);
            response.setErrorMessage(e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
