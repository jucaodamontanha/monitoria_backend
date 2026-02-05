package com.basic.monitoria.dto.auth;

public record AuthResponse(String tokenType, String accessToken, String refreshToken, long expiresInMs) {}