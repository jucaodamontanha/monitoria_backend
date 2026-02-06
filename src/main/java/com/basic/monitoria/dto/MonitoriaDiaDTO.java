package com.basic.monitoria.dto;

import java.time.LocalDate;

public record MonitoriaDiaDTO(
        LocalDate data,
        Long equipeId,
        Boolean etn,
        String observacoes,
        String outrosServicosDia
) {}