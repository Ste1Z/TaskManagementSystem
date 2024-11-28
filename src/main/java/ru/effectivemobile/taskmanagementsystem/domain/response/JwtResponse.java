package ru.effectivemobile.taskmanagementsystem.domain.response;

import lombok.Builder;

public record JwtResponse(String token, String refreshToken) {

    @Builder
    public JwtResponse {
    }
}
