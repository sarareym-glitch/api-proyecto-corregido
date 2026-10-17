package com.example.api.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración del cifrado de contraseñas. Solo usa {@code spring-security-crypto};
 * no activa Spring Security completo, por lo que los endpoints siguen abiertos.
 */
@Configuration
public class SecurityConfig {

    /**
     * Codificador BCrypt usado por {@code UserService} para guardar contraseñas.
     *
     * @return codificador de contraseñas BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
