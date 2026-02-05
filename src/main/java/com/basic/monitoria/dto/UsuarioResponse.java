package com.basic.monitoria.dto;

import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;

public record UsuarioResponse(
        Long id,
        String nomeCompleto,
        String email,
        String telefone,
        Base base,
        String login,
        Long equipeId,
        Cidade cidade,
        Funcao funcao
) {}