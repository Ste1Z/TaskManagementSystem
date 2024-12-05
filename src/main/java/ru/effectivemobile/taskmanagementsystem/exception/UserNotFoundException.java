package ru.effectivemobile.taskmanagementsystem.exception;

/**
 * Исключение используется для обозначения ошибок, если пользователь не найден.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Конструктор без параметров. Создает исключение с пустым сообщением.
     */
    public UserNotFoundException() {
    }

    /**
     * Конструктор с параметром. Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
