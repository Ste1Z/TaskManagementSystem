package ru.effectivemobile.taskmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.exception.NotAuthorizedUserException;
import ru.effectivemobile.taskmanagementsystem.exception.UserAlreadyExistsException;
import ru.effectivemobile.taskmanagementsystem.exception.UserNotFoundException;
import ru.effectivemobile.taskmanagementsystem.repository.UserRepository;
import ru.effectivemobile.taskmanagementsystem.security.JwtAuthentication;
import ru.effectivemobile.taskmanagementsystem.security.Role;
import ru.effectivemobile.taskmanagementsystem.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с пользователями.
 * Предоставляет методы для создания новых пользователей, получения текущего пользователя и интеграции с Spring Security.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Создает нового пользователя на основе переданных данных.
     *
     * @param registrationUserDto данные для регистрации нового пользователя.
     * @return {@link User}.
     * @throws UserAlreadyExistsException если пользователь с указанным именем уже существует.
     */
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

    /**
     * Возвращает пользователя по имени пользователя.
     *
     * @param username имя пользователя.
     * @return {@link User}.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username '%s' not found", username)));
    }

    /**
     * Возвращает имя текущего пользователя.
     *
     * @return {@link String}.
     * @throws NotAuthorizedUserException если пользователь не авторизован.
     */
    @Override
    public String getUsernameOfCurrentUser() throws NotAuthorizedUserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthorizedUserException("Not authorized user");
        }
        if (authentication instanceof JwtAuthentication jwtAuth) {
            return jwtAuth.getUsername();
        } else {
            throw new NotAuthorizedUserException("Not authorized user");
        }
    }

    /**
     * Возвращает текущего пользователя.
     *
     * @return {@link User}.
     */
    @Override
    public User getCurrentUser() {
        String username = this.getUsernameOfCurrentUser();
        return this.getUserByUsername(username);
    }

    /**
     * Загружает пользователя для Spring Security по имени пользователя.
     *
     * @param username имя пользователя.
     * @return {@link UserDetails}.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
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
