package ru.effectivemobile.taskmanagementsystem.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.effectivemobile.taskmanagementsystem.domain.dto.ErrorMessage;
import ru.effectivemobile.taskmanagementsystem.exception.UserAlreadyExistsException;
import ru.effectivemobile.taskmanagementsystem.exception.UserNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(HttpStatus.CONFLICT.value(), exception.getMessage()));
    }
}
