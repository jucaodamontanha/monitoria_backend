package com.basic.monitoria.controller;

import com.basic.monitoria.dto.UsuarioDTO;
import com.basic.monitoria.dto.UsuarioResponse;
import com.basic.monitoria.dto.UsuarioUpdateDTO;
import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import com.basic.monitoria.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid UsuarioDTO dto) {
        UsuarioResponse response = service.criar(dto);
        URI location = URI.create("/usuarios/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    // GET /usuarios?page=0&size=20&sort=nomeCompleto,asc&cidade=...&funcao=...&base=...&equipeId=...&nome=...&email=...&login=...
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listar(
            @PageableDefault(size = 20, sort = "nomeCompleto") Pageable pageable,
            @RequestParam(required = false) Cidade cidade,
            @RequestParam(required = false) Funcao funcao,
            @RequestParam(required = false) Base base,
            @RequestParam(required = false) Long equipeId,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String login
    ) {
        Page<UsuarioResponse> page = service.listar(pageable, cidade, funcao, base, equipeId, nome, email, login);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioResponse> buscarPorLogin(@PathVariable String login) {
        return ResponseEntity.ok(service.buscarPorLogin(login));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody @Valid UsuarioUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}