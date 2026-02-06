package com.basic.monitoria.dto;


import java.time.LocalDate;

public record CronogramaDTO(
        LocalDate dataPlanejada,
        Long equipeId,
        Boolean etn,
        String observacao
) {}
