package ru.effectivemobile.taskmanagementsystem.exception;

/**
 * Исключение используется для обозначения ошибок, если пользователь уже существует.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Конструктор без параметров. Создает исключение с пустым сообщением.
     */
    public UserAlreadyExistsException() {
    }

    /**
     * Конструктор с параметром. Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
