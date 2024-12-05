package ru.effectivemobile.taskmanagementsystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Enum для ролей пользователей в системе.
 * Реализует интерфейс {@link GrantedAuthority} из Spring Security.
 */
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    /**
     * Админ.
     */
    ROLE_ADMIN("ROLE_ADMIN"),

    /**
     * Пользователь.
     */
    ROLE_USER("ROLE_USER");

    private final String roleName;

    /**
     * Возвращает строковое представление роли пользователя.
     *
     * @return {@link String}.
     */
    @Override
    public String getAuthority() {
        return name();
    }
}