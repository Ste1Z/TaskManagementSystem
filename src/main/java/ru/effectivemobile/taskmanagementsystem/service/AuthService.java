package ru.effectivemobile.taskmanagementsystem.service;

import org.springframework.http.ResponseEntity;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.domain.response.JwtResponse;

/**
 * Сервис для управления аутентификацией и регистрацией пользователей.
 */
public interface AuthService {

    /**
     * Аутентифицирует пользователя и выдает JWT-токен.
     *
     * @param authRequest запрос с данными для аутентификации.
     * @return {@link JwtResponse}.
     */
    ResponseEntity<?> authAndGetToken(AuthRequest authRequest);

    /**
     * Проверяет данные для регистрации и регистрирует нового пользователя.
     *
     * @param registrationUserDto данные для регистрации нового пользователя.
     * @return {@link ResponseEntity}.
     */
    ResponseEntity<?> checkAndRegister(RegistrationUserDto registrationUserDto);

    /**
     * Обновляет и возвращает новый JWT-токен на основе переданного refresh-токена.
     *
     * @param refreshToken refresh-токен для обновления.
     * @return {@link JwtResponse} с новыми access и refresh токенами или null, если
     * refresh-токен недействителен.
     */
    JwtResponse refreshAndGetToken(String refreshToken);
}
