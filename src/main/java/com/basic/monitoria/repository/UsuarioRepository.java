package com.basic.monitoria.repository;

import com.basic.monitoria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario>
{
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    Optional<Usuario> findByLogin(String login);

    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByLoginAndIdNot(String login, Long id);
    Optional<Usuario> findByEmail(String email);


}
