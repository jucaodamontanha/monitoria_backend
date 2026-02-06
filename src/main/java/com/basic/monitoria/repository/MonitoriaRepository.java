package com.basic.monitoria.repository;


import com.basic.monitoria.model.Monitoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MonitoriaRepository extends JpaRepository<Monitoria, Long>, JpaSpecificationExecutor<Monitoria> {
}
