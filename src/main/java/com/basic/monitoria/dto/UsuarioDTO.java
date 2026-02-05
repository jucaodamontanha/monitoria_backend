package com.basic.monitoria.dto;


import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(

        @NotBlank
        String nomeCompleto,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String senha,

        String telefone,

        @NotNull
        Base base,

        @NotBlank
        String login,

        @NotNull
        Long equipeId,

        @NotNull
        Cidade cidade,

        @NotNull
        Funcao funcao
) {}

