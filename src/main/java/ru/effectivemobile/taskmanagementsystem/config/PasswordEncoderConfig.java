package ru.effectivemobile.taskmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфиг энкодера паролей.
 * Настраивает энкодер на BCrypt.
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Создает и возвращает бин энкодера с алгоритмом BCrypt.
     *
     * @return {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
