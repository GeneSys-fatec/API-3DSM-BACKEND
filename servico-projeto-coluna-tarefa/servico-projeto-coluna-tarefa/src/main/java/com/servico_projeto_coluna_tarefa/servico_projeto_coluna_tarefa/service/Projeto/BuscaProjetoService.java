package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BuscaProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    public List<ProjetoModel> listarPorUsuario(String usuarioId) {
        return projetoRepository.findAll();
    }

    public List<ProjetoModel> listarTodas() {
        return projetoRepository.findAll();
    }

    public Optional<ProjetoModel> buscarPorId(String id) {
        return projetoRepository.findById(id);
    }
}