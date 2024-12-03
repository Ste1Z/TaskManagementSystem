package ru.effectivemobile.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.domain.request.RefreshJwtRequest;
import ru.effectivemobile.taskmanagementsystem.domain.response.JwtResponse;
import ru.effectivemobile.taskmanagementsystem.service.impl.AuthServiceImpl;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authAndGetToken(authRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refreshAndGetToken(request.refreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return authService.checkAndRegister(registrationUserDto);
    }
}
