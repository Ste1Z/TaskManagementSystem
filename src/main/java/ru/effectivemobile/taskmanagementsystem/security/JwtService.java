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

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Long accessTokenLifeInSeconds;

    @Value("${jwt.refreshLifetime}")
    private Long jwtRefreshLifetime;

    public String generateToken(@NonNull User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationInstant =
                now.plusSeconds(accessTokenLifeInSeconds)
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

    public String generateRefreshToken(@NonNull User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant =
                now.plusSeconds(jwtRefreshLifetime)
                        .atZone(ZoneId.systemDefault())
                        .toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(refreshExpiration)
                .signWith(getSignKey())
                .compact();
    }


    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken);
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token);
    }

    private Claims getClaims(@NonNull String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

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

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtAuthentication generate(Claims claims) {
        String login = claims.getSubject();
        List<String> roleNames = claims.get("roles", List.class);
        Set<Role> roles = roleNames.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        return new JwtAuthentication(true, login, roles);
    }
}
