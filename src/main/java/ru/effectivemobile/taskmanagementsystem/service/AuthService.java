package ru.effectivemobile.taskmanagementsystem.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.security.JwtToken;
import ru.effectivemobile.taskmanagementsystem.service.impl.UserServiceImpl;

public interface AuthService {

    ResponseEntity<?> checkAndCreateAuthenticationToken(AuthRequest authRequest,
                                                        AuthenticationManager authenticationManager,
                                                        UserServiceImpl userService,
                                                        JwtToken jwtToken);

    ResponseEntity<?> checkAndRegister(RegistrationUserDto registrationUserDto, UserServiceImpl userService);
}
