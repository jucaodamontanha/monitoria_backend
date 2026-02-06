package com.basic.monitoria.dto;

import com.basic.monitoria.enuns.Cidade;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DeslocamentoDTO(
        LocalDate data,
        Cidade origem,
        Cidade destino,
        BigDecimal km,
        String observacao
) {}