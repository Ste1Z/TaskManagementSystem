package ru.effectivemobile.taskmanagementsystem.domain.response;

import lombok.Builder;

/**
 * Ответ, содержащий токен доступа и токен обновления.
 *
 * @param token        токен доступа
 * @param refreshToken токен обновления
 */
public record JwtResponse(String token, String refreshToken) {

    @Builder
    public JwtResponse {
    }
}
