package com.basic.monitoria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "monitorias")
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // supervisor que cadastrou
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supervisor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_monitoria_supervisor"))
    private Usuario supervisor;

    @NotNull
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @NotNull
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    // Equipes previstas/gerais na monitoria (além das do dia-a-dia)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "monitoria_equipes",
            joinColumns = @JoinColumn(name = "monitoria_id"),
            inverseJoinColumns = @JoinColumn(name = "equipe_id")
    )
    private Set<Equipe> equipes = new HashSet<>();

    // Campo geral para observações de outros serviços durante a monitoria
    @Lob
    @Column(name = "outros_servicos")
    private String outrosServicos;

    // filhos
    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonitoriaDia> dias = new ArrayList<>();

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Deslocamento> deslocamentos = new ArrayList<>();

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CronogramaAcompanhamento> cronograma = new ArrayList<>();

    // ---- helpers de agregação ----
    public void addDia(MonitoriaDia d) { d.setMonitoria(this); dias.add(d); }
    public void addDeslocamento(Deslocamento d) { d.setMonitoria(this); deslocamentos.add(d); }
    public void addCronograma(CronogramaAcompanhamento c) { c.setMonitoria(this); cronograma.add(c); }

    // total de equipes (calculado)
    @Transient
    public int getTotalEquipes() {
        Set<Long> ids = new HashSet<>();
        for (Equipe e : equipes) ids.add(e.getId());
        for (MonitoriaDia d : dias) if (d.getEquipe()!=null) ids.add(d.getEquipe().getId());
        for (CronogramaAcompanhamento c : cronograma) if (c.getEquipe()!=null) ids.add(c.getEquipe().getId());
        return ids.size();
    }

    // getters/setters
    public Long getId() { return id; }
    public Usuario getSupervisor() { return supervisor; }
    public void setSupervisor(Usuario supervisor) { this.supervisor = supervisor; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public Set<Equipe> getEquipes() { return equipes; }
    public void setEquipes(Set<Equipe> equipes) { this.equipes = equipes; }
    public String getOutrosServicos() { return outrosServicos; }
    public void setOutrosServicos(String outrosServicos) { this.outrosServicos = outrosServicos; }
    public List<MonitoriaDia> getDias() { return dias; }
    public void setDias(List<MonitoriaDia> dias) { this.dias = dias; }
    public List<Deslocamento> getDeslocamentos() { return deslocamentos; }
    public void setDeslocamentos(List<Deslocamento> deslocamentos) { this.deslocamentos = deslocamentos; }
    public List<CronogramaAcompanhamento> getCronograma() { return cronograma; }
    public void setCronograma(List<CronogramaAcompanhamento> cronograma) { this.cronograma = cronograma; }
}