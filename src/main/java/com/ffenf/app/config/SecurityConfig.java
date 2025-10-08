package com.ffenf.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ffenf.app.auth.JwtAuthFilter;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter, CorsConfigurationSource corsConfigurationSource) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource))
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				// Public root and static assets
				.requestMatchers("/", "/index.html", "/static/**", "/assets/**").permitAll()

				// Public health endpoints
				.requestMatchers("/health").permitAll()
				.requestMatchers("/actuator/health", "/actuator/info").permitAll()

				// Public auth and selected APIs (adjust later as needed)
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/materials/search").permitAll()
				.requestMatchers("/askhub/**").permitAll()
				.requestMatchers("/ai/**").permitAll()

				// TEMP: open everything to remove Basic Auth prompt
				.anyRequest().permitAll()
			)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			// Disable browser Basic Auth prompt
			.httpBasic(AbstractHttpConfigurer::disable);

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
