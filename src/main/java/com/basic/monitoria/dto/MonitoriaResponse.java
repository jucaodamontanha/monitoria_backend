package com.basic.monitoria.dto;


import com.basic.monitoria.enuns.Cidade;
import java.time.LocalDate;
import java.util.List;

public record MonitoriaResponse(
        Long id,
        Long supervisorId,
        String supervisorNome,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer totalEquipes,
        List<EquipeResumo> equipes,
        String outrosServicos,
        List<DiaResumo> dias,
        List<DeslocamentoResumo> deslocamentos,
        List<CronogramaResumo> cronograma
) {
    public record EquipeResumo(Long id, String nomeEquipe, Boolean etn, String loginEquipe) {}
    public record DiaResumo(Long id, LocalDate data, Long equipeId, String equipeNome, Boolean etn, String observacoes, String outrosServicosDia) {}
    public record DeslocamentoResumo(Long id, LocalDate data, Cidade origem, Cidade destino, String km, String observacao) {}
    public record CronogramaResumo(Long id, LocalDate dataPlanejada, Long equipeId, String equipeNome, Boolean etn, String observacao) {}
}
