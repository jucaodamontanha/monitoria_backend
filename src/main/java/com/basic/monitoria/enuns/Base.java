package com.basic.monitoria.enuns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Base {
    BASE_RIBERIRAO_PRETO("BASE RIBERIRAO PRETO"),
    GPON_RIBEIRAO_PRETO("GPON RIBEIRAO PRETO"),
    GPON_BAURU("GPON BAURU"),
    BASE_SOROCABA("BASE SOROCABA"),
    BASE_RIBEIRAO_VT("BASE RIBEIRAO VT"),
    BASE_PIRACICABA("BASE PIRACICABA"),
    BASE_LIMEIRA("BASE LIMEIRA"),
    BASE_SAO_JOSE_DO_RIO_PRETO("BASE SAO JOSE DO RIO PRETO"),
    BASE_BAURU("BASE BAURU"),
    BASE_SOROCABA_VT("BASE SOROCABA VT"),
    BASE_PIRACICABA_VT("BASE PIRACICABA VT"),
    BASE_VAR_PIRACICABA("BASE VAR PIRACICABA"),
    BASE_BOTUCATU_VT("BASE BOTUCATU VT"),
    BASE_BAURU_VT("BASE BAURU VT"),
    BASE_MDU_BAURU("BASE MDU BAURU"),
    BASE_BOTUCATU("BASE BOTUCATU"),
    BASE_MDU_ARARAS("BASE MDU ARARAS"),
    BASE_ARARAS_VT("BASE ARARAS VT"),
    BASE_SUMARE("BASE SUMARE"),
    BASE_MDU_PIRACICABA("BASE MDU PIRACICABA"),
    BASE_SUMARE_VT("BASE SUMARE VT"),
    BASE_VAR_BAURU("BASE VAR BAURU"),
    BASE_CAMPINAS("BASE CAMPINAS"),
    BASE_SERTAOZINHO_VT("BASE SERTAOZINHO VT"),
    BASE_PAULINIA("BASE PAULINIA"),
    DESCONEXAO_RIBEIRAO_PRETO("DESCONEXAO RIBEIRAO PRETO"),
    DESCONEXAO_BOTUCATU("DESCONEXAO BOTUCATU");

    private final String label;

    Base(String label) { this.label = label; }

    public String getLabel() { return label; }

    // Sempre que serializar para JSON, envie o NOME do enum (ex.: "BASE_SOROCABA")
    @JsonValue
    public String toJson() {
        return name();
    }

    // Ao receber JSON, aceite tanto "BASE_SOROCABA" quanto "BASE SOROCABA"
    @JsonCreator
    public static Base fromJson(String value) {
        if (value == null) return null;

        // 1) Tenta por nome exato
        try { return Base.valueOf(value); } catch (Exception ignored) {}

        // 2) Tenta por label (case-sensitive)
        for (Base b : values()) {
            if (b.label.equals(value)) return b;
        }
        // 3) Tenta por label (case-insensitive)
        for (Base b : values()) {
            if (b.label.equalsIgnoreCase(value)) return b;
        }

        throw new IllegalArgumentException("Base inválida: " + value);
    }
}