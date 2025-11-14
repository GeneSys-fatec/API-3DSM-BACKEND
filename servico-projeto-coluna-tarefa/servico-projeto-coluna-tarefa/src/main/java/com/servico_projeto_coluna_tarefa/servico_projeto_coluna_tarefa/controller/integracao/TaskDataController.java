package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.integracao;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.TarefaConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks-data")
public class TaskDataController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private TarefaConverter tarefaConverter;

    @GetMapping("/project/{projId}")
    public ResponseEntity<List<TarefaDTO>> getTasksParaMetricas(@PathVariable String projId) {

        List<TarefaModel> tarefasDoBanco = tarefaRepository.findByProjId(projId);

        if (tarefasDoBanco == null || tarefasDoBanco.isEmpty()) {
            return ResponseEntity.noContent().build();
        }


        List<TarefaDTO> tarefasDTO = tarefasDoBanco.stream()
                .map(tarefaConverter::modelParaDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tarefasDTO);
    }
}