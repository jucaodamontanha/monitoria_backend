package com.basic.monitoria.dto.auth;

public record ResetPasswordRequest(String token, String novaSenha) {}