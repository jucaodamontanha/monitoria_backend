package com.basic.monitoria.model;

import com.basic.monitoria.enuns.Cidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "monitoria_deslocamentos")
public class Deslocamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="monitoria_id", nullable=false,
            foreignKey = @ForeignKey(name="fk_deslocamento_monitoria"))
    private Monitoria monitoria;

    @NotNull
    @Column(nullable=false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=60)
    private Cidade origem;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=60)
    private Cidade destino;

    @Column(precision=10, scale=2)
    private BigDecimal km;

    @Lob
    private String observacao;

    // getters/setters
    public Long getId() { return id; }
    public Monitoria getMonitoria() { return monitoria; }
    public void setMonitoria(Monitoria monitoria) { this.monitoria = monitoria; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public Cidade getOrigem() { return origem; }
    public void setOrigem(Cidade origem) { this.origem = origem; }
    public Cidade getDestino() { return destino; }
    public void setDestino(Cidade destino) { this.destino = destino; }
    public BigDecimal getKm() { return km; }
    public void setKm(BigDecimal km) { this.km = km; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
