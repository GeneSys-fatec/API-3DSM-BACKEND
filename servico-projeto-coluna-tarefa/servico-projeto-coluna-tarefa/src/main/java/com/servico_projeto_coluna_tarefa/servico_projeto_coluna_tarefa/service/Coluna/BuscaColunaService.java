package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Coluna;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ColunaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ColunaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ColunaRepository;

@Service
public class BuscaColunaService {
    @Autowired
    private ColunaRepository colunaRepository;

    public List<ColunaDTO> listarPorProjeto(String projId) {
        List<ColunaModel> colunas = colunaRepository.findByProjIdOrderByColOrdemAsc(projId);
        return colunas.stream()
                .map(c -> new ColunaDTO(c.getColId(), c.getColTitulo(), c.getColOrdem(), c.getProjId()))
                .collect(Collectors.toList());
    }
}