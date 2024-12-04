package ru.effectivemobile.taskmanagementsystem.domain.dto;

import java.util.List;
import java.util.UUID;

public record TaskCommentsDto(UUID id, List<String> comments) {
}
