package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ProjetoRepository;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ColunaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcluiProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ColunaRepository colunaRepository;

    @Transactional
    public void deletar(String projId) {
        tarefaRepository.deleteByProjId(projId);

        colunaRepository.deleteByProjId(projId);

        projetoRepository.deleteById(projId);
    }
}