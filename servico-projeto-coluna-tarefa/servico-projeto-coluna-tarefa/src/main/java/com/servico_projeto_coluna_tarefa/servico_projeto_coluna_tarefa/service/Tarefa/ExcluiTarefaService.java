package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;

@Service
public class ExcluiTarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    public void deletar(String id) {
        tarefaRepository.deleteById(id);
    }
}