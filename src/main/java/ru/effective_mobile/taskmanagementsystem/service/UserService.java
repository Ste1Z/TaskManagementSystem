package ru.effective_mobile.taskmanagementsystem.service;

import ru.effective_mobile.taskmanagementsystem.domain.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void createUser(User user);

    User getUserById(UUID id);

    List<User> getAllUsers();
}
