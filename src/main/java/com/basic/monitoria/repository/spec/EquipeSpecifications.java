package com.basic.monitoria.repository.spec;

import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import com.basic.monitoria.model.Equipe;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EquipeSpecifications {

    public static Specification<Equipe> comFiltros(
            String nomeEquipe,
            String loginEquipe,
            Cidade cidade,
            Base base,
            Funcao funcao,
            Boolean ativo,
            Boolean etn
    ) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            if (nomeEquipe != null && !nomeEquipe.isBlank()) {
                ps.add(cb.like(cb.lower(root.get("nomeEquipe")), "%" + nomeEquipe.toLowerCase() + "%"));
            }
            if (loginEquipe != null && !loginEquipe.isBlank()) {
                ps.add(cb.like(cb.lower(root.get("loginEquipe")), "%" + loginEquipe.toLowerCase() + "%"));
            }
            if (cidade != null) {
                ps.add(cb.equal(root.get("cidade"), cidade));
            }
            if (base != null) {
                ps.add(cb.equal(root.get("base"), base));
            }
            if (funcao != null) {
                ps.add(cb.equal(root.get("funcao"), funcao));
            }
            if (ativo != null) {
                ps.add(cb.equal(root.get("ativo"), ativo));
            }
            if (etn != null) {
                ps.add(cb.equal(root.get("etn"), etn));
            }

            return cb.and(ps.toArray(new Predicate[0]));
        };
    }
}