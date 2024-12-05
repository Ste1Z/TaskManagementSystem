package ru.effectivemobile.taskmanagementsystem.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для регистрации нового пользователя.
 */
@Data
public class RegistrationUserDto {

    /**
     * Имя пользователя.
     * Не может быть пустым, длина должна быть от 3 до 50 символов.
     */
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username length must be from 3 to 50 chars")
    private String username;

    /**
     * Пароль пользователя.
     * Не может быть пустым, длина должна быть от 3 до 255 символов.
     */
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 255, message = "Password length must be from 3 to 255 chars")
    private String password;

    /**
     * Подтверждение пароля.
     * Не может быть пустым.
     */
    @NotBlank(message = "Repeat the entered password")
    private String confirmPassword;
}
