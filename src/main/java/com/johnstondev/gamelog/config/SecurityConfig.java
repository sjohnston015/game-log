package com.johnstondev.gamelog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // enable CORS with config.
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // allow all requests without authentication for now
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // disable CSRF for API testing
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
