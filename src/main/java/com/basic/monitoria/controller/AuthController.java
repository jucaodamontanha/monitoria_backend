package com.basic.monitoria.controller;

import com.basic.monitoria.dto.auth.AuthResponse;
import com.basic.monitoria.dto.auth.ForgotPasswordRequest;
import com.basic.monitoria.dto.auth.LoginRequest;
import com.basic.monitoria.dto.auth.RefreshTokenRequest;
import com.basic.monitoria.dto.auth.ResetPasswordRequest;
import com.basic.monitoria.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(service.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest req) {
        return ResponseEntity.ok(service.refresh(req));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest req) {
        service.forgotPassword(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest req) {
        service.resetPassword(req);
        return ResponseEntity.noContent().build();
    }
}