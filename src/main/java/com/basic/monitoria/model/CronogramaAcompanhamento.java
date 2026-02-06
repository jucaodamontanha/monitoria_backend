package com.basic.monitoria.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "monitoria_cronograma")
public class CronogramaAcompanhamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="monitoria_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_cronograma_monitoria"))
    private Monitoria monitoria;

    @Column(name="data_planejada", nullable=false)
    private LocalDate dataPlanejada;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="equipe_id", foreignKey=@ForeignKey(name="fk_cronograma_equipe"))
    private Equipe equipe;

    @Column(name="etn")
    private Boolean etn; // se os técnicos previstos são ETN

    @Lob
    private String observacao;

    // getters/setters
    public Long getId() { return id; }
    public Monitoria getMonitoria() { return monitoria; }
    public void setMonitoria(Monitoria monitoria) { this.monitoria = monitoria; }
    public LocalDate getDataPlanejada() { return dataPlanejada; }
    public void setDataPlanejada(LocalDate dataPlanejada) { this.dataPlanejada = dataPlanejada; }
    public Equipe getEquipe() { return equipe; }
    public void setEquipe(Equipe equipe) { this.equipe = equipe; }
    public Boolean getEtn() { return etn; }
    public void setEtn(Boolean etn) { this.etn = etn; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}