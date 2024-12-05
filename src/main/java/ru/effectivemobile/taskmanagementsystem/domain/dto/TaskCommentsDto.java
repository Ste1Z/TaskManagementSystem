package ru.effectivemobile.taskmanagementsystem.domain.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO для ответа на добавление комментария.
 * Содержит id задачи и список комментариев.
 */
public record TaskCommentsDto(UUID id, List<String> comments) {
}
