package com.basic.monitoria.dto;

import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipeUpdateDTO(
        @NotBlank @Size(max = 120)
        String nomeEquipe,

        @NotBlank @Size(max = 80)
        String loginEquipe,

        @NotNull
        Cidade cidade,

        @NotNull
        Base base,

        Boolean etn,

        @NotNull
        Funcao funcao,

        @NotNull
        Boolean ativo
) {}