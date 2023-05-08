package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.example.config.security.SessionUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Optional;

@SpringBootApplication
@OpenAPIDefinition
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@EnableWebSecurity
@EnableAsync
@RequiredArgsConstructor
public class Main {
    private final SessionUser sessionUser;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public AuditorAware<Integer> auditorAware() {
        return () -> Optional.of(sessionUser.getId());
    }

}