package ru.effectivemobile.taskmanagementsystem.handler;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.effectivemobile.taskmanagementsystem.exception.AuthException;
import ru.effectivemobile.taskmanagementsystem.exception.ErrorMessage;
import ru.effectivemobile.taskmanagementsystem.exception.TaskNotFoundException;
import ru.effectivemobile.taskmanagementsystem.exception.UserAlreadyExistsException;
import ru.effectivemobile.taskmanagementsystem.exception.UserNotFoundException;

import java.util.stream.Collectors;

/**
 * Класс для перехвата исключений, возникающих в контроллерах, и формирования ответов.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    /**
     * Обрабатывает исключения, связанные с ошибками валидации.
     *
     * @param exception исключение {@link MethodArgumentNotValidException}
     * @return {@link ResponseEntity} с ошибкой и статусом 400 (BAD_REQUEST)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    /**
     * Обрабатывает исключения аутентификации, например, ошибки при логине или регистрации.
     *
     * @param exception исключение {@link AuthException}
     * @return {@link ResponseEntity} с ошибкой и статусом 400 (BAD_REQUEST)
     */
    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ErrorMessage> handleAuthException(AuthException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    /**
     * Обрабатывает исключение, когда пользователь не найден.
     *
     * @param exception исключение {@link UserNotFoundException}
     * @return {@link ResponseEntity} с ошибкой и статусом 404 (NOT_FOUND)
     */
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    /**
     * Обрабатывает исключение, когда имя пользователя не найдено.
     *
     * @param exception исключение {@link UsernameNotFoundException}
     * @return {@link ResponseEntity} с ошибкой и статусом 404 (NOT_FOUND)
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorMessage> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    /**
     * Обрабатывает исключение, когда пользователь уже существует.
     *
     * @param exception исключение {@link UserAlreadyExistsException}
     * @return {@link ResponseEntity} с ошибкой и статусом 409 (CONFLICT)
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(HttpStatus.CONFLICT.value(), exception.getMessage()));
    }

    /**
     * Обрабатывает исключение, когда задача не найдена.
     *
     * @param exception исключение {@link TaskNotFoundException}
     * @return {@link ResponseEntity} с ошибкой и статусом 404 (NOT_FOUND)
     */
    @ExceptionHandler(TaskNotFoundException.class)
    protected ResponseEntity<ErrorMessage> handleTaskNotFoundException(TaskNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }
}
