package ru.effectivemobile.taskmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.exception.UserAlreadyExistsException;
import ru.effectivemobile.taskmanagementsystem.exception.UserNotFoundException;
import ru.effectivemobile.taskmanagementsystem.repository.UserRepository;
import ru.effectivemobile.taskmanagementsystem.security.Role;
import ru.effectivemobile.taskmanagementsystem.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createNewUser(RegistrationUserDto registrationUserDto) throws UserAlreadyExistsException {
        Optional<User> possibleUser = userRepository.findByUsername(registrationUserDto.getUsername());
        User user;
        if (possibleUser.isEmpty()) {
            user = User.builder()
                    .username(registrationUserDto.getUsername())
                    .password(passwordEncoder.encode(registrationUserDto.getPassword()))
                    .roles(Set.of(Role.ROLE_USER))
                    .build();
        } else {
            throw new UserAlreadyExistsException(String.format("User with username '%s' already exists", registrationUserDto.getUsername()));
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id '%s' not found", id)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username '%s' not found", username)));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with name '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList())
        );
    }
}
