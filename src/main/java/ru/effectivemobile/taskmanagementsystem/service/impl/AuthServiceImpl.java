package ru.effectivemobile.taskmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanagementsystem.domain.dto.ErrorMessage;
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.domain.request.AuthRequest;
import ru.effectivemobile.taskmanagementsystem.domain.response.AuthResponse;
import ru.effectivemobile.taskmanagementsystem.domain.response.UserDto;
import ru.effectivemobile.taskmanagementsystem.security.JwtToken;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final UserServiceImpl userService;
    private final JwtToken jwtToken;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> checkAndRegister(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Passwords do not matches"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername()));
    }

    public ResponseEntity<?> checkAndCreateAuthenticationToken(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Wrong username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.username());
        String token = jwtToken.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
