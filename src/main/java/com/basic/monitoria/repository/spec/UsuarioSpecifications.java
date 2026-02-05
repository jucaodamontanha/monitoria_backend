package com.basic.monitoria.repository.spec;

import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import com.basic.monitoria.model.Usuario;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSpecifications {

    public static Specification<Usuario> comFiltros(
            Cidade cidade,
            Funcao funcao,
            Base base,
            Long equipeId,
            String nome,
            String email,
            String login
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (cidade != null) {
                predicates.add(cb.equal(root.get("cidade"), cidade));
            }
            if (funcao != null) {
                predicates.add(cb.equal(root.get("funcao"), funcao));
            }
            if (base != null) {
                predicates.add(cb.equal(root.get("base"), base));
            }
            if (equipeId != null) {
                predicates.add(cb.equal(root.get("equipe").get("id"), equipeId));
            }
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nomeCompleto")), "%" + nome.toLowerCase() + "%"));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (login != null && !login.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("login")), "%" + login.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}