package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditaProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    public ProjetoModel salvar(ProjetoModel projeto) {
        return projetoRepository.save(projeto);
    }
}