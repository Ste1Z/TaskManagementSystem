package ru.effective_mobile.taskmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effective_mobile.taskmanagementsystem.domain.entity.User;
import ru.effective_mobile.taskmanagementsystem.exception.UserNotFoundException;
import ru.effective_mobile.taskmanagementsystem.repository.UserRepository;
import ru.effective_mobile.taskmanagementsystem.service.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
