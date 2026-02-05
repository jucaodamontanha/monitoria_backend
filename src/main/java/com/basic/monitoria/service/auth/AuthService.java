package com.basic.monitoria.service.auth;

import com.basic.monitoria.dto.auth.*;
import com.basic.monitoria.model.PasswordResetToken;
import com.basic.monitoria.model.Usuario;
import com.basic.monitoria.repository.PasswordResetTokenRepository;
import com.basic.monitoria.repository.UsuarioRepository;
import com.basic.monitoria.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.expiration}")
    private long accessExpirationMs;

    public AuthService(AuthenticationManager authManager,
                       CustomUserDetailsService userDetailsService,
                       JwtService jwtService,
                       UsuarioRepository usuarioRepository,
                       PasswordResetTokenRepository resetTokenRepository,
                       PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.login(), req.senha())
            );
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Login ou senha inválidos");
        }

        UserDetails user = userDetailsService.loadUserByUsername(req.login());
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);
        return new AuthResponse("Bearer", access, refresh, accessExpirationMs);
    }

    public AuthResponse refresh(RefreshTokenRequest req) {
        String token = req.refreshToken();
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Refresh token ausente");
        }
        String username = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isRefreshToken(token) || !jwtService.isTokenValid(token, user)) {
            throw new IllegalArgumentException("Refresh token inválido ou expirado");
        }

        String newAccess = jwtService.generateAccessToken(user);
        String newRefresh = jwtService.generateRefreshToken(user);
        return new AuthResponse("Bearer", newAccess, newRefresh, accessExpirationMs);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest req) {
        Usuario usuario = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Email não encontrado"));

        // Invalida tokens anteriores do usuário (opcional)
        resetTokenRepository.deleteByUsuarioId(usuario.getId());

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUsuario(usuario);
        prt.setToken(UUID.randomUUID().toString());
        prt.setExpiresAt(Instant.now().plusSeconds(30 * 60)); // 30 min
        resetTokenRepository.save(prt);

        // TODO: enviar email de verdade usando Spring Mail (por enquanto apenas log)
        System.out.println("[DEV] Token de reset para " + usuario.getEmail() + ": " + prt.getToken());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest req) {
        PasswordResetToken token = resetTokenRepository.findByToken(req.token())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (token.isUsed()) {
            throw new IllegalArgumentException("Token já utilizado");
        }
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expirado");
        }

        Usuario usuario = token.getUsuario();
        usuario.setSenha(passwordEncoder.encode(req.novaSenha()));
        usuarioRepository.save(usuario);

        token.setUsed(true);
        resetTokenRepository.save(token);
    }
}