package ru.effectivemobile.taskmanagementsystem.service;

import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для создания новых пользователей и получения информации о текущем пользователе.
 */
public interface UserService {

    /**
     * Создает нового пользователя на основе переданных данных.
     *
     * @param registrationUserDto данные для регистрации нового пользователя.
     * @return {@link User}.
     */
    User createNewUser(RegistrationUserDto registrationUserDto);

    /**
     * Возвращает пользователя по его имени пользователя.
     *
     * @param username имя пользователя.
     * @return {@link User}.
     */
    User getUserByUsername(String username);

    /**
     * Возвращает имя текущего пользователя.
     *
     * @return {@link String}.
     */
    String getUsernameOfCurrentUser();

    /**
     * Возвращает текущего пользователя.
     *
     * @return {@link User}.
     */
    User getCurrentUser();
}
