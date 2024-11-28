package ru.effectivemobile.taskmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.service.impl.AuthServiceImpl;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        return authService.checkAndCreateAuthenticationToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.checkAndRegister(registrationUserDto);
    }
}
