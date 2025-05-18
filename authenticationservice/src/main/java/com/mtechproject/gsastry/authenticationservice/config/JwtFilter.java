package com.mtechproject.gsastry.authenticationservice.config;

import com.mtechproject.gsastry.authenticationservice.service.CustomUserDetailsService;
import com.mtechproject.gsastry.authenticationservice.service.JWTService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        // Skip authentication for certain endpoints
        if (requestURI.contains("v3/api-docs") || requestURI.contains("/swagger-ui") || requestURI.contains("h2-console") || requestURI.equals("/api/signup")
                || requestURI.equals("/auth/login")
                || requestURI.equals("/auth/signup")
                || requestURI.contains("/auth/validate-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract and validate JWT token
        String token = request.getHeader("Authorization");
        String jwtToken = null;
        String userName = null;

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            jwtToken = token.substring(7).trim();
            try {
                userName = jwtService.extractUserName(jwtToken); // Extract username from JWT
            } catch (MalformedJwtException e) {
                // Handle invalid JWT format
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token format");
                return;
            } catch (Exception e) {
                // Handle other exceptions that may occur during JWT extraction
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized access: " + e.getMessage());
                return;
            }
        } else {
            // If the token is missing, return Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization token is missing");
            return;
        }

        // Authenticate only if username exists and no existing auth in the context
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Determine user role from JWT
            String role = extractUserRole(jwtToken);
            UserDetailsService userDetailsService = new CustomUserDetailsService();

            // Authenticate if a valid role-based UserDetailsService is found
            if (userDetailsService != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if (userDetails == null) {
                    logger.warn("UserDetails for username: " + userName + " is null.");
                } else {
                    logger.debug("UserDetails loaded successfully for username: " + userName);
                }

                if (jwtService.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.warn("JWT validation failed for username: " + userName);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid JWT token");
                    return;
                }
            } else {
                logger.warn("No matching UserDetailsService found for role: " + role);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized access");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // Extracts role from JWT token (modify as needed based on JWTService)
    private String extractUserRole(String jwtToken) {
        try {
            List<String> roles = jwtService.extractRoles(jwtToken);
            return roles.isEmpty() ? null : roles.get(0);
        } catch (Exception e) {
            logger.error("Error extracting roles from JWT: " + e.getMessage());
            return null;
        }
    }
}
