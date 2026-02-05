package com.basic.monitoria.model;

import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
        name = "equipes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_equipes_login", columnNames = "login_equipe")
        }
)
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome_equipe", nullable = false, length = 120)
    private String nomeEquipe;

    @NotBlank
    @Column(name = "login_equipe", nullable = false, length = 80)
    private String loginEquipe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 60)
    private Cidade cidade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 80)
    private Base base;

    // pode ser null se quiser 3 estados: SIM/NÃO/INDEFINIDO
    @Column
    private Boolean etn;

    @Column(nullable = false)
    private boolean ativo = true;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 60)
    private Funcao funcao;

    public Equipe() {}

    public Equipe(Long id, String nomeEquipe, String loginEquipe, Cidade cidade, Base base, Boolean etn, boolean ativo, Funcao funcao) {
        this.id = id;
        this.nomeEquipe = nomeEquipe;
        this.loginEquipe = loginEquipe;
        this.cidade = cidade;
        this.base = base;
        this.etn = etn;
        this.ativo = ativo;
        this.funcao = funcao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeEquipe() { return nomeEquipe; }
    public void setNomeEquipe(String nomeEquipe) { this.nomeEquipe = nomeEquipe; }
    public String getLoginEquipe() { return loginEquipe; }
    public void setLoginEquipe(String loginEquipe) { this.loginEquipe = loginEquipe; }
    public Cidade getCidade() { return cidade; }
    public void setCidade(Cidade cidade) { this.cidade = cidade; }
    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
    public Boolean getEtn() { return etn; }
    public void setEtn(Boolean etn) { this.etn = etn; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Funcao getFuncao() { return funcao; }
    public void setFuncao(Funcao funcao) { this.funcao = funcao; }
}