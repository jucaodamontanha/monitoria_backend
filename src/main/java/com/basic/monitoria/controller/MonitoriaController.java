package com.basic.monitoria.controller;



import com.basic.monitoria.dto.MonitoriaCreateDTO;
import com.basic.monitoria.dto.MonitoriaResponse;
import com.basic.monitoria.dto.MonitoriaUpdateDTO;
import com.basic.monitoria.service.MonitoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/monitorias")
public class MonitoriaController {

    private final MonitoriaService service;

    public MonitoriaController(MonitoriaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MonitoriaResponse> criar(@RequestBody @Valid MonitoriaCreateDTO dto) {
        MonitoriaResponse resp = service.criar(dto);
        return ResponseEntity.created(URI.create("/monitorias/" + resp.id())).body(resp);
    }

    @GetMapping
    public ResponseEntity<Page<MonitoriaResponse>> listar(
            @PageableDefault(size = 20, sort = "dataInicio") Pageable pageable,
            @RequestParam(required = false) Long supervisorId,
            @RequestParam(required = false) Long equipeId,
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim
    ) {
        return ResponseEntity.ok(service.listar(pageable, supervisorId, equipeId, inicio, fim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoriaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitoriaResponse> atualizar(@PathVariable Long id,
                                                       @RequestBody @Valid MonitoriaUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}