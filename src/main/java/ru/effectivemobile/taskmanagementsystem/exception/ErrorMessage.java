package ru.effectivemobile.taskmanagementsystem.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorMessage {

    private int errorCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
