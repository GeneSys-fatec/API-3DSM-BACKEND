package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ResponsavelTarefa;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;


@Service
public class EditaTarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private BuscaTarefaService buscaTarefaService;

    
    public TarefaModel atualizarTarefa(String tarId, TarefaDTO dto) {
        TarefaModel tarefa = buscaTarefaService.buscarPorId(tarId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + tarId));

        tarefa.setTarTitulo(dto.getTarTitulo());
        tarefa.setTarDescricao(dto.getTarDescricao());
        tarefa.setTarStatus(dto.getTarStatus());
        tarefa.setTarPrioridade(dto.getTarPrioridade());
        tarefa.setTarPrazo(dto.getTarPrazo());
        tarefa.setTarDataAtualizacao(dto.getTarDataAtualizacao());
        tarefa.setProjId(dto.getProjId());
        
        if (dto.getResponsaveis() != null && !dto.getResponsaveis().isEmpty()) {
            List<ResponsavelTarefa> responsaveisEntidade = dto.getResponsaveis().stream()
                    .map(dtoResp -> new ResponsavelTarefa(dtoResp.getUsuId(), dtoResp.getUsuNome()))
                    .collect(Collectors.toList());
            tarefa.setResponsaveis(responsaveisEntidade);
        } else {
            tarefa.setResponsaveis(new ArrayList<>());
        }
        
        if ("Concluída".equalsIgnoreCase(dto.getTarStatus())) {
            String dataConclusao = java.time.LocalDate.now().toString();
            tarefa.setTarDataConclusao(dataConclusao);
            java.time.LocalDate prazo = java.time.LocalDate.parse(tarefa.getTarPrazo());
            java.time.LocalDate conclusao = java.time.LocalDate.parse(dataConclusao);
            tarefa.setConcluidaNoPrazo(!conclusao.isAfter(prazo));
        } else {
            tarefa.setTarDataConclusao(null);
            tarefa.setConcluidaNoPrazo(null);
        }
        
        TarefaModel tarefaAtualizada = tarefaRepository.save(tarefa);

        return tarefaAtualizada;
    }

    public TarefaModel atualizar(TarefaModel tarefa) {
        return tarefaRepository.save(tarefa);
    }
}