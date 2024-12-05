package ru.effectivemobile.taskmanagementsystem.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskmanagementsystem.exception.ErrorMessage;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.domain.response.JwtResponse;
import ru.effectivemobile.taskmanagementsystem.domain.dto.UserDto;
import ru.effectivemobile.taskmanagementsystem.exception.AuthException;
import ru.effectivemobile.taskmanagementsystem.security.JwtService;
import ru.effectivemobile.taskmanagementsystem.service.AuthService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация сервиса аутентификации и регистрации пользователей.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, String> refreshStorage = new ConcurrentHashMap<>();

    /**
     * Проверяет корректность данных для регистрации и регистрирует нового пользователя.
     *
     * @param registrationUserDto данные для регистрации нового пользователя.
     * @return {@link ResponseEntity}.
     */
    @Override
    public ResponseEntity<?> checkAndRegister(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Passwords do not matches"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername()));
    }

    /**
     * Аутентифицирует пользователя и генерирует JWT-токен и refresh-токен.
     *
     * @param authRequest запрос с данными для аутентификации.
     * @return {@link JwtResponse}.
     * @throws AuthException если пароль некорректен.
     */
    @Override
    public JwtResponse authAndGetToken(AuthRequest authRequest) {
        User user = userService.getUserByUsername(authRequest.username());
        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            throw new AuthException("Incorrect password");
        }
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshStorage.put(user.getUsername(), refreshToken);
        return JwtResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Обновляет и возвращает JWT-токен на основе refresh-токена.
     *
     * @param refreshToken refresh-токен.
     * @return {@link JwtResponse} или null, если refresh-токен недействителен.
     */
    public JwtResponse refreshAndGetToken(String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtService.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getUserByUsername(username);
                final String accessToken = jwtService.generateToken(user);
                return new JwtResponse(accessToken, jwtService.generateRefreshToken(user));
            }
        }
        return new JwtResponse(null, null);
    }
}
