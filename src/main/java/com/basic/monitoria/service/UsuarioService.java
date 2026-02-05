package com.basic.monitoria.service;

import com.basic.monitoria.dto.UsuarioDTO;
import com.basic.monitoria.dto.UsuarioResponse;
import com.basic.monitoria.dto.UsuarioUpdateDTO;
import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import com.basic.monitoria.model.Equipe;
import com.basic.monitoria.model.Usuario;
import com.basic.monitoria.repository.EquipeRepository;
import com.basic.monitoria.repository.UsuarioRepository;
import com.basic.monitoria.repository.spec.UsuarioSpecifications;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final EquipeRepository equipeRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuarioService(EquipeRepository equipeRepository,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.equipeRepository = equipeRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse criar(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (usuarioRepository.existsByLogin(dto.login())) {
            throw new IllegalArgumentException("Login já cadastrado");
        }

        Equipe equipe = equipeRepository.findById(dto.equipeId())
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.nomeCompleto());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setTelefone(dto.telefone());
        usuario.setBase(dto.base());
        usuario.setLogin(dto.login());
        usuario.setEquipe(equipe);
        usuario.setCidade(dto.cidade());
        usuario.setFuncao(dto.funcao());

        Usuario salvo = usuarioRepository.save(usuario);
        return toResponse(salvo);
    }

    public Page<UsuarioResponse> listar(
            Pageable pageable,
            Cidade cidade,
            Funcao funcao,
            Base base,
            Long equipeId,
            String nome,
            String email,
            String login
    ) {
        var spec = UsuarioSpecifications.comFiltros(cidade, funcao, base, equipeId, nome, email, login);
        return usuarioRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return toResponse(usuario);
    }

    public UsuarioResponse buscarPorLogin(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // validações de unicidade
        if (usuarioRepository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (usuarioRepository.existsByLoginAndIdNot(dto.login(), id)) {
            throw new IllegalArgumentException("Login já cadastrado");
        }

        // equipe
        Equipe equipe = equipeRepository.findById(dto.equipeId())
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));

        usuario.setNomeCompleto(dto.nomeCompleto());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());
        usuario.setBase(dto.base());
        usuario.setLogin(dto.login());
        usuario.setEquipe(equipe);
        usuario.setCidade(dto.cidade());
        usuario.setFuncao(dto.funcao());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario salvo = usuarioRepository.save(usuario);
        return toResponse(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNomeCompleto(),
                u.getEmail(),
                u.getTelefone(),
                u.getBase(),
                u.getLogin(),
                u.getEquipe().getId(),
                u.getCidade(),
                u.getFuncao()
        );
    }
}
