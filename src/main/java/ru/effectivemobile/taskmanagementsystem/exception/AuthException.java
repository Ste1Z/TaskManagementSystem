package ru.effectivemobile.taskmanagementsystem.exception;

/**
 * Исключение используется для обозначения ошибок аутентификации.
 */
public class AuthException extends RuntimeException {

    /**
     * Конструктор без параметров. Создает исключение с пустым сообщением.
     */
    public AuthException() {
        super();
    }

    /**
     * Конструктор с параметром. Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public AuthException(String message) {
        super(message);
    }
}
