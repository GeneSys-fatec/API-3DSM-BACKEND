package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.ProjetoConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ProjetoDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ProjetoRepository;

@Service
public class BuscaProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private ProjetoConverter projetoConverter;

    public List<ProjetoModel> listarPorUsuario(String usuarioId) {
        return projetoRepository.findAll();
    }

    public List<ProjetoModel> listarTodas() {
        return projetoRepository.findAll();
    }

    public Optional<ProjetoModel> buscarPorId(String id) {
        return projetoRepository.findById(id);
    }

    public List<ProjetoDTO> buscarPorListaDeIds(List<String> ids) {
        List<ProjetoModel> projetos = projetoRepository.findAllByProjIdIn(ids);
        return projetos.stream()
                .map(projetoConverter::modelParaDto)
                .collect(Collectors.toList());
    }
}