package com.jolivan.archivomotorclasicobackend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // Marks this class as a configuration class for Spring.
@EnableWebSecurity // Enables web security for the application.
public class WebSecurityConfiguration  {
    @Autowired
    private final UserDetailsService userDetailsService;

    public WebSecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean // Indicates that this method returns a Spring bean.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection, common in stateless REST APIs.
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/user", "POST"), new AntPathRequestMatcher("/resources", "GET")).permitAll() // Allow POST requests to /user without authentication
                        .anyRequest().authenticated() // Ensures all requests are authenticated.
                )
                .httpBasic(withDefaults()) // Enables HTTP Basic Authentication with default settings.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Configures session management to be stateless.
        return http.build(); // Builds and returns the SecurityFilterChain.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}