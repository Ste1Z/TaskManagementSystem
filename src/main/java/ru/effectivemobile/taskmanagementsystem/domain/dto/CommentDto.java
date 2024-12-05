package ru.effectivemobile.taskmanagementsystem.domain.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для сохранения комментария в задаче.
 */
@Getter
@Setter
@AllArgsConstructor
public class CommentDto {

    /**
     * Текст комментария.
     * Не может быть пустым, длина должна быть от 1 до 255 символов.
     */
    @NotBlank(message = "Comment cannot be blank")
    @Size(min = 1, max = 255, message = "Comment length must be from 1 to 255 chars")
    private String comment;
}
