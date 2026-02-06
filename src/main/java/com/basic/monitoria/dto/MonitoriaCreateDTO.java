package com.basic.monitoria.dto;


import java.time.LocalDate;
import java.util.List;

public record MonitoriaCreateDTO(
        Long supervisorId,
        LocalDate dataInicio,
        LocalDate dataFim,
        List<Long> equipesIds,                 // geral
        String outrosServicos,                 // geral
        List<MonitoriaDiaDTO> dias,
        List<DeslocamentoDTO> deslocamentos,
        List<CronogramaDTO> cronograma
) {}