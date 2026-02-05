package com.basic.monitoria.dto;

import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;

public record EquipeDTO(
        Long id,
        String nomeEquipe,
        String loginEquipe,
        Cidade cidade,
        Base base,
        Boolean etn,
        boolean ativo,
        Funcao funcao
) {
}
