package ru.effectivemobile.taskmanagementsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.effectivemobile.taskmanagementsystem.exception.ErrorMessage;
import ru.effectivemobile.taskmanagementsystem.exception.JwtException;

import java.io.IOException;

/**
 * Реализация фильтра Spring Security для обработки JWT-аутентификации.
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtService jwtService;

    /**
     * Обрабатывает запрос: извлекает JWT-токен, проверяет его и устанавливает аутентификацию.
     *
     * @param request  объект запроса Servlet
     * @param response объект ответа Servlet
     * @param fc       цепочка фильтров
     * @throws IOException      если возникает ошибка ввода-вывода
     * @throws ServletException если возникает ошибка, специфичная для сервлета
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        try {
            String token = getTokenFromRequest((HttpServletRequest) request);
            if (token != null && jwtService.validateAccessToken(token)) {
                Claims claims = jwtService.getAccessClaims(token);
                JwtAuthentication jwtInfoToken = jwtService.generate(claims);
                SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
            }
            fc.doFilter(request, response);
        } catch (JwtException e) {
            catchJwtExceptionAndReturnErrorMessage(e, (HttpServletResponse) response);
        }
    }

    /**
     * Извлекает JWT-токен из заголовка Authorization запроса.
     *
     * @param request объект HTTP-запроса
     * @return {@link String} или null, если токен отсутствует или некорректен
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    /**
     * Обрабатывает исключение JwtException и записывает сообщение об ошибке в ответ.
     *
     * @param jwtException исключение JwtException
     * @param response     объект HTTP-ответа
     * @throws IOException если возникает ошибка записи в ответ
     */
    private void catchJwtExceptionAndReturnErrorMessage(JwtException jwtException, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), jwtException.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String jsonResponse = objectMapper.writeValueAsString(errorMessage);
        response.getWriter().write(jsonResponse);
    }
}
