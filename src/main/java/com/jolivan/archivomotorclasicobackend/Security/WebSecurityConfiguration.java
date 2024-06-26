package com.jolivan.archivomotorclasicobackend.Security;

import com.jolivan.archivomotorclasicobackend.User.Controllers.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // Marks this class as a configuration class for Spring.
@EnableWebSecurity // Enables web security for the application.
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration  {
    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection, common in stateless REST APIs.
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(
                                new AntPathRequestMatcher("/user", "GET"),
                                new AntPathRequestMatcher("/user", "POST"),
                                new AntPathRequestMatcher("/user/authenticate", "POST"),
                                new AntPathRequestMatcher("/user/forgotpassword", "POST"),
                                new AntPathRequestMatcher("/user/resetpassword", "POST"),
                                new AntPathRequestMatcher("/resources", "GET"),
                                new AntPathRequestMatcher("/resources/properties/*", "GET"),
                                new AntPathRequestMatcher("/resources/*", "GET"),
                                new AntPathRequestMatcher("/resources/imagesearch", "POST"),
                                new AntPathRequestMatcher("/collections", "GET"),
                                new AntPathRequestMatcher("/collections/*", "GET"),
                                new AntPathRequestMatcher("/comments", "GET"),
                                new AntPathRequestMatcher("/upvotes", "GET"),
                                new AntPathRequestMatcher("/upvotes/*", "GET")
                        ).permitAll() // Allow POST requests to /user without authentication
                        .requestMatchers(new AntPathRequestMatcher("/resourcenodes")).hasRole("ADMIN") // Allow GET requests to /resourcenodes without authentication
                        .anyRequest().authenticated() // Ensures all requests are authenticated.
                )
                .httpBasic(withDefaults()) // Enables HTTP Basic Authentication with default settings.

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Configures session management to be stateless.
//                .cors(cors -> cors.disable()); // Disables CORS protection, common in stateless REST APIs.
//                .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Adds a filter to validate API keys.
        return http.build(); // Builds and returns the SecurityFilterChain.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}