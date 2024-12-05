package ru.effectivemobile.taskmanagementsystem.domain.request;

/**
 * Запрос на обновление токена.
 *
 * @param refreshToken токен обновления
 */
public record RefreshJwtRequest(String refreshToken) {
}
