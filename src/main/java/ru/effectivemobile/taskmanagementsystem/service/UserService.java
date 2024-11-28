package ru.effectivemobile.taskmanagementsystem.service;

import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;

import java.util.UUID;

public interface UserService {

    User createNewUser(RegistrationUserDto registrationUserDto);

    User getUserById(UUID id);

    User getUserByUsername(String username);
}
