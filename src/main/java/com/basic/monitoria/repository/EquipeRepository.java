package com.basic.monitoria.repository;

import com.basic.monitoria.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipeRepository extends JpaRepository<Equipe, Long>, JpaSpecificationExecutor<Equipe> {
    boolean existsByLoginEquipe(String loginEquipe);
    boolean existsByLoginEquipeAndIdNot(String loginEquipe, Long id);
}