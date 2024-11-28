package ru.effectivemobile.taskmanagementsystem.exception;

public class NotAuthorizedUserException extends RuntimeException {

    public NotAuthorizedUserException() {
    }

    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
