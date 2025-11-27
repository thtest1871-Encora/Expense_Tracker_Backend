package com.example.expense.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.expense.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()

            .authorizeHttpRequests(auth -> auth

                // INTERNAL ONLY — Auth Service triggers these
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/user/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/user/token/**").permitAll()

                // USER PROFILE — must be authenticated (JWT)
                .requestMatchers(HttpMethod.GET, "/user/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/user/**").authenticated()

                // Any other endpoints allow
                .anyRequest().permitAll()
            )

            // Add JWT filter BEFORE UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

