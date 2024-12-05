package ru.effectivemobile.taskmanagementsystem.domain.dto;

import java.util.UUID;

/**
 * DTO для отображения пользователя после регистрации.
 * Содержит id и имя пользователя.
 */
public record UserDto(UUID id, String username) {
}
