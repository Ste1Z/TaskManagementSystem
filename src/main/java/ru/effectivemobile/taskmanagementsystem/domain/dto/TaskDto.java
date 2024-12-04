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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private UUID id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 50, message = "Max length are 50 chars")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 256, message = "Max length are 256 chars")
    private String description;

    @NotBlank(message = "Status cannot be blank")
    @EnumValidation(enumClass = Status.class, message = "Invalid status")
    @Enumerated(EnumType.STRING)
    private String status;

    @NotBlank(message = "Priority cannot be blank")
    @EnumValidation(enumClass = Priority.class, message = "Invalid priority")
    @Enumerated(EnumType.STRING)
    private String priority;

    private List<String> comment;

    @NotBlank(message = "Executor cannot be blank")
    private String executor;
}
