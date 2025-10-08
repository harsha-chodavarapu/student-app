package com.ffenf.app.auth;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ffenf.app.repo.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserRepository userRepository;

	public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String path = request.getRequestURI();
		
		System.out.println("JWT Filter - Processing path: " + path);
		
		// Skip JWT processing for public endpoints
		if (isPublicEndpoint(path)) {
			System.out.println("JWT Filter - Skipping public endpoint: " + path);
			filterChain.doFilter(request, response);
			return;
		}
		
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		System.out.println("JWT Filter - Authorization header: " + (header != null ? "Present" : "Missing"));
		
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			System.out.println("JWT Filter - Token length: " + token.length());
			try {
				Claims claims = jwtService.parse(token);
				String email = claims.getSubject();
				String role = claims.get("role", String.class);
				System.out.println("JWT Filter - Parsed email: " + email + ", role: " + role);
				
				Authentication auth = new UsernamePasswordAuthenticationToken(
					email,
					null,
					List.of(new SimpleGrantedAuthority("ROLE_" + role))
				);
				SecurityContextHolder.getContext().setAuthentication(auth);
				System.out.println("JWT Filter - Authentication set successfully");
			} catch (Exception e) {
				System.err.println("JWT Filter - Token parsing failed: " + e.getMessage());
				SecurityContextHolder.clearContext();
			}
		} else {
			System.err.println("JWT Filter - No valid Bearer token found");
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}
	
    private boolean isPublicEndpoint(String path) {
        // Public health and auth endpoints
        if (path.startsWith("/health") || path.startsWith("/actuator/health") ||
            path.startsWith("/auth/register") || path.startsWith("/auth/login") ||
            path.startsWith("/ai/")) {
            return true;
        }

        // Allow public access to materials search and read-only GET endpoints
        if (path.equals("/materials/search")) {
            return true;
        }
        // Public GET of material metadata or file/download
        if (path.startsWith("/materials/") &&
            (path.endsWith("/file") || path.endsWith("/download") || path.matches("/materials/[a-f0-9\-]+$"))) {
            return true;
        }

        // Everything else, including /materials/upload, requires JWT
        return false;
    }
}


