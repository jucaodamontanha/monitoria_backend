package com.basic.monitoria.dto;

import java.time.LocalDate;
import java.util.List;

public record MonitoriaUpdateDTO(
        LocalDate dataInicio,
        LocalDate dataFim,
        List<Long> equipesIds,
        String outrosServicos,
        List<MonitoriaDiaDTO> dias,
        List<DeslocamentoDTO> deslocamentos,
        List<CronogramaDTO> cronograma
) {}
