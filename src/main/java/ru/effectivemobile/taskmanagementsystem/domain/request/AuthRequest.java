package ru.effectivemobile.taskmanagementsystem.domain.request;

/**
 * Запрос на аутентификацию.
 *
 * @param username имя пользователя
 * @param password пароль пользователя
 */
public record AuthRequest(String username, String password) {
}
