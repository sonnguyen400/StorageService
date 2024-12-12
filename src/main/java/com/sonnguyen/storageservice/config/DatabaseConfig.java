package com.sonnguyen.storageservice.config;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaRepositories("com.sonnguyen.storageservice.repository")
@EntityScan("com.sonnguyen.storageservice.model")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DatabaseConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return Optional.of("");
            }
            return Optional.of(auth.getPrincipal().toString());
        };
    }
    @Bean
    public HibernatePropertiesCustomizer hibernateCustomizer(StatementInspector statementInspector) {
        return (properties) -> properties.put(AvailableSettings.STATEMENT_INSPECTOR, statementInspector);
    }
}
