package com.basic.monitoria.repository.spec;

import com.basic.monitoria.model.Monitoria;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;

public class MonitoriaSpecifications {

    public static Specification<Monitoria> porSupervisor(Long supervisorId) {
        return (root, q, cb) -> supervisorId == null
                ? cb.conjunction()
                : cb.equal(root.get("supervisor").get("id"), supervisorId);
    }

    public static Specification<Monitoria> porPeriodo(LocalDate inicio, LocalDate fim) {
        return (root, q, cb) -> {
            if (inicio == null && fim == null) {
                return cb.conjunction();
            }
            if (inicio != null && fim != null) {
                // interseção de períodos: (dataInicio <= fim) AND (dataFim >= inicio)
                return cb.and(
                        cb.lessThanOrEqualTo(root.get("dataInicio"), fim),
                        cb.greaterThanOrEqualTo(root.get("dataFim"), inicio)
                );
            }
            if (inicio != null) {
                // qualquer monitoria cujo fim seja >= início filtrado
                return cb.greaterThanOrEqualTo(root.get("dataFim"), inicio);
            } else {
                // qualquer monitoria cujo início seja <= fim filtrado
                return cb.lessThanOrEqualTo(root.get("dataInicio"), fim);
            }
        };
    }

    /**
     * Filtra monitorias que envolvam uma equipe (na lista geral "equipes" OU nos "dias.equipe").
     * Usa LEFT JOIN + distinct(true) para evitar duplicações.
     */
    public static Specification<Monitoria> porEquipe(Long equipeId) {
        return (Root<Monitoria> root, CriteriaQuery<?> q, CriteriaBuilder cb) -> {
            if (equipeId == null) return cb.conjunction();

            // para não retornar monitorias duplicadas quando houver múltiplos dias/joins
            q.distinct(true);

            // join com coleção de equipes gerais
            var equipesJoin = root.join("equipes", JoinType.LEFT);

            // join com a lista de dias e a equipe do dia
            var diasJoin = root.join("dias", JoinType.LEFT);
            var diaEquipePath = diasJoin.get("equipe");

            return cb.or(
                    cb.equal(equipesJoin.get("id"), equipeId),
                    cb.equal(diaEquipePath.get("id"), equipeId)
            );
        };
    }
}