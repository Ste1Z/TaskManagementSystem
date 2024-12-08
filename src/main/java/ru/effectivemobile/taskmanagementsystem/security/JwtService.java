package ru.effectivemobile.taskmanagementsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.exception.JwtException;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для работы с JWT-токенами, включая их генерацию, валидацию и извлечение данных.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Long accessTokenLife;

    @Value("${jwt.refreshLifetime}")
    private Long refreshTokenLifetime;

    /**
     * Генерирует JWT токен доступа для указанного пользователя.
     *
     * @param user объект пользователя, для которого создается токен.
     * @return {@link String}.
     */
    public String generateToken(@NonNull User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationInstant =
                now.plusSeconds(accessTokenLife)
                        .atZone(ZoneId.systemDefault())
                        .toInstant();
        Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(accessExpiration)
                .signWith(getSignKey())
                .claim("roles", user.getRoles())
                .claim("username", user.getUsername())
                .compact();
    }

    /**
     * Генерирует refresh-токен для указанного пользователя.
     *
     * @param user объект пользователя, для которого создается refresh-токен.
     * @return {@link String}.
     */
    public String generateRefreshToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant =
                now.plusSeconds(refreshTokenLifetime)
                        .atZone(ZoneId.systemDefault())
                        .toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(refreshExpiration)
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Проверяет валидность токена доступа.
     *
     * @param accessToken строка токена доступа.
     * @return true, если токен валиден; false, если нет.
     */
    public boolean validateAccessToken(String accessToken) {
        checkToken(accessToken, "Incorrect token");
        return validateToken(accessToken);
    }

    /**
     * Проверяет валидность refresh-токена.
     *
     * @param refreshToken строка refresh-токена.
     * @return true, если токен валиден; false, если нет.
     */
    public boolean validateRefreshToken(String refreshToken) {
        checkToken(refreshToken, "Incorrect refresh token");
        return validateToken(refreshToken);
    }

    /**
     * Извлекает claims из токена доступа.
     *
     * @param token строка токена.
     * @return {@link Claims}.
     */
    public Claims getAccessClaims(String token) {
        checkToken(token, "Incorrect token");
        return getClaims(token);
    }

    /**
     * Извлекает claims из refresh-токена.
     *
     * @param token строка токена.
     * @return {@link Claims}.
     */
    public Claims getRefreshClaims(String token) {
        checkToken(token, "Incorrect token");
        return getClaims(token);
    }

    /**
     * Генерирует объект {@link JwtAuthentication} на основе claims из токена.
     *
     * @param claims объект claims, извлеченный из токена.
     * @return {@link JwtAuthentication}.
     */
    public JwtAuthentication generate(Claims claims) {
        String login = claims.getSubject();
        List<String> roleNames = claims.get("roles", List.class);
        Set<Role> roles = roleNames.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        return new JwtAuthentication(true, login, roles);
    }

    /**
     * Извлекает claims из указанного токена.
     *
     * @param token строка токена.
     * @return {@link Claims}.
     */
    private Claims getClaims(String token) {
        checkToken(token, "Incorrect token");
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Проверяет валидность указанного токена.
     *
     * @param token строка токена.
     * @return true, если токен валиден.
     * @throws JwtException если токен недействителен (истек, некорректный формат, неподдерживаемый или имеет неправильную подпись).
     */
    private boolean validateToken(@NonNull String token) throws JwtException {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException
                 | UnsupportedJwtException
                 | MalformedJwtException
                 | SignatureException e) {
            throw new JwtException("Incorrect jwt token");
        }
    }

    /**
     * Получает ключ подписи из строки секрета.
     *
     * @return {@link Key}.
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Проверяет валидность токена.
     *
     * @param token   токен, который необходимо проверить.
     * @param message сообщение об ошибке, если токен недействителен.
     * @throws JwtException, если токен равен null или пустой строке.
     */
    private void checkToken(String token, String message) throws JwtException {
        if (token == null || token.isEmpty()) {
            throw new JwtException(message);
        }
    }
}
