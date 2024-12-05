package ru.effectivemobile.taskmanagementsystem.exception;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Болванка сообщения об ошибке.
 * Используется для передачи информации об ошибках в ответах API.
 */
@Data
public class ErrorMessage {

    /**
     * Код ошибки.
     */
    private int errorCode;

    /**
     * Сообщение об ошибке.
     */
    private String message;

    /**
     * Время возникновения ошибки.
     */
    private LocalDateTime timestamp;

    /**
     * Конструктор для создания объекта сообщения об ошибке.
     *
     * @param errorCode код ошибки
     * @param message   текст сообщения об ошибке
     */
    public ErrorMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
