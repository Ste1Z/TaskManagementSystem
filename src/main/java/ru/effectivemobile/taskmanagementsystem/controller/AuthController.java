package ru.effectivemobile.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.domain.request.RefreshJwtRequest;
import ru.effectivemobile.taskmanagementsystem.domain.response.JwtResponse;
import ru.effectivemobile.taskmanagementsystem.service.impl.AuthServiceImpl;

/**
 * Контроллер для обработки запросов, связанных с аутентификацией, обновлением токенов и регистрацией пользователей.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    /**
     * Аутентифицирует пользователя и выдает JWT-токен.
     *
     * @param authRequest объект с данными для аутентификации.
     * @return {@link ResponseEntity} с JWT-токеном.
     */
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authAndGetToken(authRequest));
    }

    /**
     * Обновляет JWT-токен на основе рефреш-токена.
     *
     * @param request объект с рефреш-токеном.
     * @return {@link ResponseEntity} с новым JWT-токеном.
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refreshAndGetToken(request.refreshToken());
        return ResponseEntity.ok(token);
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param registrationUserDto объект с данными для регистрации.
     * @return {@link ResponseEntity} с DTO нового пользователя.
     */
    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return authService.checkAndRegister(registrationUserDto);
    }
}
