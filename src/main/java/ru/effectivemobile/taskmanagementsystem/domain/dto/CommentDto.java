package ru.effectivemobile.taskmanagementsystem.domain.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {

    @NotBlank(message = "Comment cannot be blank")
    @Size(min = 1, max = 255, message = "Comment length must be from 1 to 255 chars")
    private String comment;
}
