package ru.effectivemobile.taskmanagementsystem.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationUserDto {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username length must be from 3 to 50 chars")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 255, message = "Password length must be from 3 to 255 chars")
    private String password;

    @NotBlank(message = "Repeat the entered password")
    private String confirmPassword;
}
