package ru.effectivemobile.taskmanagementsystem.service;

import org.springframework.http.ResponseEntity;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.domain.response.JwtResponse;

public interface AuthService {

    JwtResponse authAndGetToken(AuthRequest authRequest);

    ResponseEntity<?> checkAndRegister(RegistrationUserDto registrationUserDto);
}
