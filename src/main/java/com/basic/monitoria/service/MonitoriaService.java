package com.basic.monitoria.service;



import com.basic.monitoria.dto.MonitoriaCreateDTO;
import com.basic.monitoria.dto.MonitoriaResponse;
import com.basic.monitoria.dto.MonitoriaUpdateDTO;
import com.basic.monitoria.model.*;
import com.basic.monitoria.repository.EquipeRepository;
import com.basic.monitoria.repository.MonitoriaRepository;
import com.basic.monitoria.repository.UsuarioRepository;
import com.basic.monitoria.repository.spec.MonitoriaSpecifications;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MonitoriaService {

    private final MonitoriaRepository monitoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipeRepository equipeRepository;

    public MonitoriaService(MonitoriaRepository monitoriaRepository,
                            UsuarioRepository usuarioRepository,
                            EquipeRepository equipeRepository) {
        this.monitoriaRepository = monitoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipeRepository = equipeRepository;
    }

    @Transactional
    public MonitoriaResponse criar(MonitoriaCreateDTO dto) {
        Usuario supervisor = usuarioRepository.findById(dto.supervisorId())
                .orElseThrow(() -> new IllegalArgumentException("Supervisor não encontrado"));

        // (Opcional) validar função
        switch (supervisor.getFuncao()) {
            case SUPERVISOR, COORDENADOR, ADMINISTRADOR -> {}
            default -> throw new IllegalArgumentException("Apenas supervisor/coordenador/administrador pode criar monitoria");
        }

        if (dto.dataInicio() == null || dto.dataFim() == null || dto.dataInicio().isAfter(dto.dataFim())) {
            throw new IllegalArgumentException("Período inválido (data início deve ser <= data fim)");
        }

        Monitoria m = new Monitoria();
        m.setSupervisor(supervisor);
        m.setDataInicio(dto.dataInicio());
        m.setDataFim(dto.dataFim());
        m.setOutrosServicos(dto.outrosServicos());

        // Equipes gerais
        if (dto.equipesIds() != null && !dto.equipesIds().isEmpty()) {
            List<Equipe> equipes = equipeRepository.findAllById(dto.equipesIds());
            if (equipes.size() != dto.equipesIds().size()) {
                throw new IllegalArgumentException("Alguma equipe informada não foi encontrada");
            }
            m.getEquipes().addAll(equipes);
        }

        // Dias
        if (dto.dias() != null) {
            dto.dias().forEach(d -> {
                if (d.data() == null) throw new IllegalArgumentException("Dia de acompanhamento sem data");
                if (d.data().isBefore(dto.dataInicio()) || d.data().isAfter(dto.dataFim())) {
                    throw new IllegalArgumentException("Dia fora do período da monitoria");
                }
                MonitoriaDia md = new MonitoriaDia();
                md.setData(d.data());
                md.setEtn(d.etn());
                md.setObservacoes(d.observacoes());
                md.setOutrosServicosDia(d.outrosServicosDia());
                if (d.equipeId() != null) {
                    Equipe equipe = equipeRepository.findById(d.equipeId())
                            .orElseThrow(() -> new IllegalArgumentException("Equipe do dia não encontrada"));
                    md.setEquipe(equipe);
                }
                m.addDia(md);
            });
        }

        // Deslocamentos
        if (dto.deslocamentos() != null) {
            dto.deslocamentos().forEach(dd -> {
                if (dd.data() == null) throw new IllegalArgumentException("Deslocamento sem data");
                Deslocamento des = new Deslocamento();
                des.setData(dd.data());
                des.setOrigem(dd.origem());
                des.setDestino(dd.destino());
                des.setKm(dd.km());
                des.setObservacao(dd.observacao());
                m.addDeslocamento(des);
            });
        }

        // Cronograma
        if (dto.cronograma() != null) {
            dto.cronograma().forEach(c -> {
                if (c.dataPlanejada() == null) throw new IllegalArgumentException("Cronograma sem data planejada");
                CronogramaAcompanhamento cr = new CronogramaAcompanhamento();
                cr.setDataPlanejada(c.dataPlanejada());
                cr.setEtn(c.etn());
                cr.setObservacao(c.observacao());
                if (c.equipeId() != null) {
                    Equipe e = equipeRepository.findById(c.equipeId())
                            .orElseThrow(() -> new IllegalArgumentException("Equipe do cronograma não encontrada"));
                    cr.setEquipe(e);
                }
                m.addCronograma(cr);
            });
        }

        Monitoria salvo = monitoriaRepository.save(m);
        return toResponse(salvo);
    }

    public Page<MonitoriaResponse> listar(Pageable pageable, Long supervisorId, Long equipeId,
                                          java.time.LocalDate inicio, java.time.LocalDate fim) {
        Specification<Monitoria> spec = Specification.where(
                MonitoriaSpecifications.porSupervisor(supervisorId)
                        .and(MonitoriaSpecifications.porPeriodo(inicio, fim))
                        .and(MonitoriaSpecifications.porEquipe(equipeId))
        );
        return monitoriaRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public MonitoriaResponse buscarPorId(Long id) {
        Monitoria m = monitoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Monitoria não encontrada"));
        return toResponse(m);
    }

    @Transactional
    public MonitoriaResponse atualizar(Long id, MonitoriaUpdateDTO dto) {
        Monitoria m = monitoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Monitoria não encontrada"));

        if (dto.dataInicio() != null && dto.dataFim() != null && dto.dataInicio().isAfter(dto.dataFim())) {
            throw new IllegalArgumentException("Período inválido na atualização");
        }

        if (dto.dataInicio() != null) m.setDataInicio(dto.dataInicio());
        if (dto.dataFim() != null) m.setDataFim(dto.dataFim());
        if (dto.outrosServicos() != null) m.setOutrosServicos(dto.outrosServicos());

        if (dto.equipesIds() != null) {
            m.getEquipes().clear();
            if (!dto.equipesIds().isEmpty()) {
                List<Equipe> equipes = equipeRepository.findAllById(dto.equipesIds());
                if (equipes.size() != dto.equipesIds().size()) {
                    throw new IllegalArgumentException("Alguma equipe informada não foi encontrada");
                }
                m.getEquipes().addAll(equipes);
            }
        }

        if (dto.dias() != null) {
            m.getDias().clear();
            dto.dias().forEach(d -> {
                MonitoriaDia md = new MonitoriaDia();
                if (d.data() == null) throw new IllegalArgumentException("Dia sem data");
                md.setData(d.data());
                if (d.equipeId() != null) {
                    Equipe e = equipeRepository.findById(d.equipeId())
                            .orElseThrow(() -> new IllegalArgumentException("Equipe do dia não encontrada"));
                    md.setEquipe(e);
                }
                md.setEtn(d.etn());
                md.setObservacoes(d.observacoes());
                md.setOutrosServicosDia(d.outrosServicosDia());
                m.addDia(md);
            });
        }

        if (dto.deslocamentos() != null) {
            m.getDeslocamentos().clear();
            dto.deslocamentos().forEach(dd -> {
                Deslocamento des = new Deslocamento();
                des.setData(dd.data());
                des.setOrigem(dd.origem());
                des.setDestino(dd.destino());
                des.setKm(dd.km());
                des.setObservacao(dd.observacao());
                m.addDeslocamento(des);
            });
        }

        if (dto.cronograma() != null) {
            m.getCronograma().clear();
            dto.cronograma().forEach(c -> {
                CronogramaAcompanhamento cr = new CronogramaAcompanhamento();
                cr.setDataPlanejada(c.dataPlanejada());
                if (c.equipeId() != null) {
                    Equipe e = equipeRepository.findById(c.equipeId())
                            .orElseThrow(() -> new IllegalArgumentException("Equipe do cronograma não encontrada"));
                    cr.setEquipe(e);
                }
                cr.setEtn(c.etn());
                cr.setObservacao(c.observacao());
                m.addCronograma(cr);
            });
        }

        Monitoria salvo = monitoriaRepository.save(m);
        return toResponse(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        if (!monitoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("Monitoria não encontrada");
        }
        monitoriaRepository.deleteById(id);
    }

    private MonitoriaResponse toResponse(Monitoria m) {
        var equipes = m.getEquipes().stream()
                .map(e -> new MonitoriaResponse.EquipeResumo(e.getId(), e.getNomeEquipe(), e.getEtn(), e.getLoginEquipe()))
                .toList();

        var dias = m.getDias().stream()
                .map(d -> new MonitoriaResponse.DiaResumo(
                        d.getId(),
                        d.getData(),
                        d.getEquipe() != null ? d.getEquipe().getId() : null,
                        d.getEquipe() != null ? d.getEquipe().getNomeEquipe() : null,
                        d.getEtn(),
                        d.getObservacoes(),
                        d.getOutrosServicosDia()
                )).toList();

        var deslocs = m.getDeslocamentos().stream()
                .map(des -> new MonitoriaResponse.DeslocamentoResumo(
                        des.getId(),
                        des.getData(),
                        des.getOrigem(),
                        des.getDestino(),
                        des.getKm() != null ? des.getKm().toPlainString() : null,
                        des.getObservacao()
                )).toList();

        var crono = m.getCronograma().stream()
                .map(c -> new MonitoriaResponse.CronogramaResumo(
                        c.getId(),
                        c.getDataPlanejada(),
                        c.getEquipe() != null ? c.getEquipe().getId() : null,
                        c.getEquipe() != null ? c.getEquipe().getNomeEquipe() : null,
                        c.getEtn(),
                        c.getObservacao()
                )).toList();

        return new MonitoriaResponse(
                m.getId(),
                m.getSupervisor().getId(),
                m.getSupervisor().getNomeCompleto(),
                m.getDataInicio(),
                m.getDataFim(),
                m.getTotalEquipes(),
                equipes,
                m.getOutrosServicos(),
                dias,
                deslocs,
                crono
        );
    }
}
