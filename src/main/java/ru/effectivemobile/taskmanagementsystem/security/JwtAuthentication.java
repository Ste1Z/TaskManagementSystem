package ru.effectivemobile.taskmanagementsystem.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

/**
 * Реализация интерфейса Authentication из Spring Security.
 * Используется для представления информации об аутентификации пользователя на основе JWT-токена.
 */
@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private String username;
    private Set<Role> roles;

    /**
     * Конструктор для создания объекта JwtAuthentication.
     *
     * @param authenticated статус аутентификации
     * @param username      имя пользователя
     * @param roles         роли пользователя
     */
    public JwtAuthentication(boolean authenticated, String username, Set<Role> roles) {
        this.authenticated = authenticated;
        this.username = username;
        this.roles = roles;
    }

    /**
     * Возвращает коллекцию ролей пользователя.
     *
     * @return {@link Collection}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * Возвращает учетные данные пользователя. Не используется.
     *
     * @return всегда null.
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Возвращает детали аутентификации. Не используется.
     *
     * @return всегда null.
     */
    @Override
    public Object getDetails() {
        return null;
    }

    /**
     * Возвращает основную информацию о пользователе.
     *
     * @return текущий {@link Object} JwtAuthentication.
     */
    @Override
    public Object getPrincipal() {
        return this;
    }

    /**
     * Проверяет, аутентифицирован ли пользователь.
     *
     * @return true, если аутентифицирован, false - если нет.
     */
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Устанавливает статус аутентификации пользователя.
     *
     * @param isAuthenticated новый статус аутентификации
     * @throws IllegalArgumentException если передан некорректный статус
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return {@link String}.
     */
    @Override
    public String getName() {
        return username;
    }
}
