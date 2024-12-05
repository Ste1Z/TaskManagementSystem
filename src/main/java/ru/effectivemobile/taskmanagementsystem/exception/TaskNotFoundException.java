package ru.effectivemobile.taskmanagementsystem.exception;

/**
 * Исключение используется для обозначения ошибок, если задача не найдена.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Конструктор без параметров. Создает исключение с пустым сообщением.
     */
    public TaskNotFoundException() {
    }

    /**
     * Конструктор с параметром. Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public TaskNotFoundException(String message) {
        super(message);
    }
}
