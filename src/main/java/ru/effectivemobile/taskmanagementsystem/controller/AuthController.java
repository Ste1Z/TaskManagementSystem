package ru.effectivemobile.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.domain.request.RefreshJwtRequest;
import ru.effectivemobile.taskmanagementsystem.domain.response.JwtResponse;
import ru.effectivemobile.taskmanagementsystem.exception.ErrorMessage;
import ru.effectivemobile.taskmanagementsystem.service.impl.AuthServiceImpl;

/**
 * Контроллер для обработки запросов, связанных с аутентификацией, обновлением токенов и регистрацией пользователей.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    /**
     * Аутентифицирует пользователя на основе предоставленных данных
     * и возвращает JWT-токен вместе с refresh-токеном.
     * Токены также устанавливаются в cookies ответа.
     *
     * @param authRequest объект с данными для аутентификации (логин и пароль).
     * @return {@link ResponseEntity}, содержащий {@link JwtResponse} с токенами.
     */
    @Operation(summary = "User authentication", description = "Validates user credentials and returns a JWT access token and a refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect password",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "User with username not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        return authService.authAndGetToken(authRequest);
    }

    /**
     * Обновляет JWT-токен на основе рефреш-токена.
     *
     * @param request объект с рефреш-токеном.
     * @return {@link ResponseEntity} с новым JWT-токеном.
     */
    @Operation(summary = "Refresh JWT token", description = "Uses a refresh token to generate a new JWT access token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Incorrect refresh token",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/refresh")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse token = authService.refreshAndGetToken(request.refreshToken());
        return ResponseEntity.ok(token);
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param registrationUserDto объект с данными для регистрации.
     * @return {@link ResponseEntity} с DTO нового пользователя.
     */
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RegistrationUserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid registration data",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "409", description = "User with username already exists",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtResponse.class)))
            })
    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return authService.checkAndRegister(registrationUserDto);
    }
}
