package com.sonnguyen.storageservice.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain configSecurityWithExternalIdp(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request
                    .requestMatchers("/api/v1/public/*",
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-resources/**").permitAll()
                    .anyRequest().authenticated();
        }).oauth2ResourceServer(resourceServerConfig->{
            resourceServerConfig.jwt(Customizer.withDefaults());
        });
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
