package com.basic.monitoria.controller;

import com.basic.monitoria.dto.EquipeCreateDTO;
import com.basic.monitoria.dto.EquipeResponse;
import com.basic.monitoria.dto.EquipeUpdateDTO;
import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import com.basic.monitoria.service.EquipeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

    private final EquipeService service;

    public EquipeController(EquipeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EquipeResponse> criar(@RequestBody @Valid EquipeCreateDTO dto) {
        EquipeResponse resp = service.criar(dto);
        return ResponseEntity.created(URI.create("/equipes/" + resp.id())).body(resp);
    }

    // GET /equipes?nomeEquipe=...&loginEquipe=...&cidade=...&base=...&funcao=...&ativo=true&etn=false&sort=nomeEquipe,asc
    @GetMapping
    public ResponseEntity<Page<EquipeResponse>> listar(
            @PageableDefault(size = 20, sort = "nomeEquipe") Pageable pageable,
            @RequestParam(required = false) String nomeEquipe,
            @RequestParam(required = false) String loginEquipe,
            @RequestParam(required = false) Cidade cidade,
            @RequestParam(required = false) Base base,
            @RequestParam(required = false) Funcao funcao,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Boolean etn
    ) {
        return ResponseEntity.ok(
                service.listar(pageable, nomeEquipe, loginEquipe, cidade, base, funcao, ativo, etn)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeResponse> atualizar(@PathVariable Long id,
                                                    @RequestBody @Valid EquipeUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
