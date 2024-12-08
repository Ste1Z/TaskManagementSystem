package ru.effectivemobile.taskmanagementsystem.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.dto.UserDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.domain.response.JwtResponse;
import ru.effectivemobile.taskmanagementsystem.exception.AuthException;
import ru.effectivemobile.taskmanagementsystem.exception.ErrorMessage;
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

    @Value("${jwt.lifetime}")
    private Long accessTokenLife;

    @Value("${jwt.refreshLifetime}")
    private Long refreshTokenLifetime;

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
     * Аутентифицирует пользователя на основе предоставленных данных и возвращает JWT-токен
     * вместе с refresh-токеном. Токены также добавляются в cookies ответа.
     *
     * @param authRequest объект с данными для аутентификации (логин и пароль).
     * @return {@link ResponseEntity}, содержащий {@link JwtResponse} с токенами.
     * @throws AuthException если пароль некорректен.
     */
    @Override
    public ResponseEntity<?> authAndGetToken(AuthRequest authRequest) {
        User user = userService.getUserByUsername(authRequest.username());
        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            throw new AuthException("Incorrect password");
        }
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshStorage.put(user.getUsername(), refreshToken);
        ResponseCookie jwtCookie = ResponseCookie.from("JWT", token)
                .httpOnly(false)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(accessTokenLife)
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(false)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(refreshTokenLifetime)
                .build();
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(jwtResponse);
    }

    /**
     * Обновляет и возвращает JWT-токен на основе refresh-токена.
     *
     * @param refreshToken refresh-токен.
     * @return {@link JwtResponse} или null, если refresh-токен недействителен.
     */
    @Override
    public JwtResponse refreshAndGetToken(String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)) {
            Claims claims = jwtService.getRefreshClaims(refreshToken);
            String username = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userService.getUserByUsername(username);
                String accessToken = jwtService.generateToken(user);
                return new JwtResponse(accessToken, jwtService.generateRefreshToken(user));
            }
        }
        return new JwtResponse(null, null);
    }
}
