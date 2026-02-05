package com.basic.monitoria.service;

import com.basic.monitoria.dto.EquipeCreateDTO;
import com.basic.monitoria.dto.EquipeResponse;
import com.basic.monitoria.dto.EquipeUpdateDTO;
import com.basic.monitoria.enuns.Base;
import com.basic.monitoria.enuns.Cidade;
import com.basic.monitoria.enuns.Funcao;
import com.basic.monitoria.model.Equipe;
import com.basic.monitoria.repository.EquipeRepository;
import com.basic.monitoria.repository.spec.EquipeSpecifications;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;

    public EquipeService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    @Transactional
    public EquipeResponse criar(EquipeCreateDTO dto) {
        if (equipeRepository.existsByLoginEquipe(dto.loginEquipe())) {
            throw new IllegalArgumentException("Login da equipe já cadastrado");
        }

        Equipe e = new Equipe();
        e.setNomeEquipe(dto.nomeEquipe());
        e.setLoginEquipe(dto.loginEquipe());
        e.setCidade(dto.cidade());
        e.setBase(dto.base());
        e.setEtn(dto.etn());
        e.setFuncao(dto.funcao());
        e.setAtivo(dto.ativo() == null ? true : dto.ativo());

        Equipe salvo = equipeRepository.save(e);
        return toResponse(salvo);
    }

    public Page<EquipeResponse> listar(
            Pageable pageable,
            String nomeEquipe,
            String loginEquipe,
            Cidade cidade,
            Base base,
            Funcao funcao,
            Boolean ativo,
            Boolean etn
    ) {
        Specification<Equipe> spec = EquipeSpecifications.comFiltros(
                nomeEquipe, loginEquipe, cidade, base, funcao, ativo, etn
        );
        return equipeRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public EquipeResponse buscarPorId(Long id) {
        Equipe e = equipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));
        return toResponse(e);
    }

    @Transactional
    public EquipeResponse atualizar(Long id, EquipeUpdateDTO dto) {
        Equipe e = equipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe não encontrada"));

        if (equipeRepository.existsByLoginEquipeAndIdNot(dto.loginEquipe(), id)) {
            throw new IllegalArgumentException("Login da equipe já cadastrado");
        }

        e.setNomeEquipe(dto.nomeEquipe());
        e.setLoginEquipe(dto.loginEquipe());
        e.setCidade(dto.cidade());
        e.setBase(dto.base());
        e.setEtn(dto.etn());
        e.setFuncao(dto.funcao());
        e.setAtivo(dto.ativo());

        Equipe salvo = equipeRepository.save(e);
        return toResponse(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new IllegalArgumentException("Equipe não encontrada");
        }
        equipeRepository.deleteById(id);
    }

    private EquipeResponse toResponse(Equipe e) {
        return new EquipeResponse(
                e.getId(),
                e.getNomeEquipe(),
                e.getLoginEquipe(),
                e.getCidade(),
                e.getBase(),
                e.getEtn(),
                e.isAtivo(),
                e.getFuncao()
        );
    }
}
