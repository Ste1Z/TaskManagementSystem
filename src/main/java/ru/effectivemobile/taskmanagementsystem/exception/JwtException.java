package ru.effectivemobile.taskmanagementsystem.exception;

/**
 * Исключение используется для обозначения ошибок с JWT-токеном.
 */
public class JwtException extends RuntimeException {

    /**
     * Конструктор без параметров. Создает исключение с пустым сообщением.
     */
    public JwtException() {
    }

    /**
     * Конструктор с параметром. Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public JwtException(String message) {
        super(message);
    }
}

