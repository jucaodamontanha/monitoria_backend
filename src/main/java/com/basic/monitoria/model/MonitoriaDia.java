package com.basic.monitoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "monitoria_dias")
public class MonitoriaDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="monitoria_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_monitoria_dia_monitoria"))
    private Monitoria monitoria;

    @NotNull
    @Column(nullable=false)
    private LocalDate data;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="equipe_id", foreignKey=@ForeignKey(name="fk_monitoria_dia_equipe"))
    private Equipe equipe;

    // se o técnico acompanhado era ETN nesse dia
    @Column(name="etn")
    private Boolean etn;

    @Lob
    private String observacoes;

    // campo “outros serviços” do dia (separado do geral, opcional)
    @Lob
    @Column(name="outros_servicos_dia")
    private String outrosServicosDia;

    // getters/setters
    public Long getId() { return id; }
    public Monitoria getMonitoria() { return monitoria; }
    public void setMonitoria(Monitoria monitoria) { this.monitoria = monitoria; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public Equipe getEquipe() { return equipe; }
    public void setEquipe(Equipe equipe) { this.equipe = equipe; }
    public Boolean getEtn() { return etn; }
    public void setEtn(Boolean etn) { this.etn = etn; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getOutrosServicosDia() { return outrosServicosDia; }
    public void setOutrosServicosDia(String outrosServicosDia) { this.outrosServicosDia = outrosServicosDia; }
}
