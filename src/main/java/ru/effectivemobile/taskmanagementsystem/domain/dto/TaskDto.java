package ru.effectivemobile.taskmanagementsystem.domain.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Priority;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Status;
import ru.effectivemobile.taskmanagementsystem.util.validation.EnumValidation;

import java.util.List;
import java.util.UUID;

/**
 * DTO для сохранения задачи.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    /**
     * Id задачи.
     */
    private UUID id;

    /**
     * Заголовок задачи.
     * Не может быть пустым, максимальная длина - 50 символов.
     */
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 50, message = "Max length are 50 chars")
    private String title;

    /**
     * Описание задачи в виде строки.
     * Не может быть пустым, максимальная длина - 256 символов.
     */
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 256, message = "Max length are 256 chars")
    private String description;

    /**
     * Статус задачи в виде строки.
     * Не может быть пустым, должен быть валидным значением из перечисления Status.
     */
    @NotBlank(message = "Status cannot be blank")
    @EnumValidation(enumClass = Status.class, message = "Invalid status")
    @Enumerated(EnumType.STRING)
    private String status;

    /**
     * Приоритет задачи.
     * Не может быть пустым, должен быть валидным значением из перечисления Priority.
     */
    @NotBlank(message = "Priority cannot be blank")
    @EnumValidation(enumClass = Priority.class, message = "Invalid priority")
    @Enumerated(EnumType.STRING)
    private String priority;

    /**
     * {@link List<String>} Список комментариев к задаче.
     */
    private List<String> comments;

    /**
     * Автор задачи (имя пользователя).
     * Не может быть пустым.
     */
    @NotBlank(message = "Executor cannot be blank")
    private String author;

    /**
     * Исполнитель задачи (имя пользователя).
     * Не может быть пустым.
     */
    @NotBlank(message = "Executor cannot be blank")
    private String executor;
}
