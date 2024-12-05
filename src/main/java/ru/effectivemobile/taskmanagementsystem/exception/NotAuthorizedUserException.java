package ru.effectivemobile.taskmanagementsystem.exception;

/**
 * Исключение используется для обозначения ошибок неавторизованного пользователя.
 */
public class NotAuthorizedUserException extends RuntimeException {

    /**
     * Конструктор без параметров. Создает исключение с пустым сообщением.
     */
    public NotAuthorizedUserException() {
    }

    /**
     * Конструктор с параметром. Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
