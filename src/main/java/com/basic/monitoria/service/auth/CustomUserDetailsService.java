package com.basic.monitoria.service.auth;

import com.basic.monitoria.model.Usuario;
import com.basic.monitoria.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado"));

        // Usa a função como papel. Ex: ROLE_TECNICO, ROLE_SUPERVISOR...
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + u.getFuncao().name())
        );

        return new User(u.getLogin(), u.getSenha(), authorities);
    }
}