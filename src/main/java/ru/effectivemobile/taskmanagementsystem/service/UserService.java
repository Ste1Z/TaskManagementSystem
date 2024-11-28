package ru.effectivemobile.taskmanagementsystem.service;

import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;

public interface UserService {

    User createNewUser(RegistrationUserDto registrationUserDto);
}
