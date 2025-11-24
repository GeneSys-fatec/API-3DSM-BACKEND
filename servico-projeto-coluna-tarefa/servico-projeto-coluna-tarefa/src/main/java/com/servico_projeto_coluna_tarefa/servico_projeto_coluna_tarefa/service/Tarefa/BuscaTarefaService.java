package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;

@Service
public class BuscaTarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<TarefaModel> listarTodas() {
        return tarefaRepository.findAll();
    }

    public List<TarefaModel> listarPorProjetoUnico(String projId) {
        if (projId == null || projId.isEmpty()) {
            return Collections.emptyList();
        }

        List<TarefaModel> todasAsTarefas = tarefaRepository.findAll();
        return todasAsTarefas.stream()
                .filter(tarefa -> projId.equals(tarefa.getProjId()))
                .collect(Collectors.toList());
    }

    public Optional<TarefaModel> buscarPorId(String id) {
        return tarefaRepository.findById(id);
    }

    public List<TarefaModel> listarTarefasPorResponsavel(String usuId) {
        return tarefaRepository.findByResponsaveisUsuId(usuId);
    }

    public List<TarefaModel> buscarTarefasComFiltros(String projId, String termo, List<String> responsaveisIds) {
        List<TarefaModel> tarefasDoProjeto = listarPorProjetoUnico(projId);

        return tarefasDoProjeto.stream()
                .filter(tarefa -> {
                    if (termo != null && !termo.trim().isEmpty()) {
                        String termoLimpo = termo.trim().toLowerCase();
                        String titulo = tarefa.getTarTitulo() != null ? tarefa.getTarTitulo().toLowerCase() : "";

                        if (!titulo.contains(termoLimpo)) {
                            return false;
                        }
                    }

                    if (responsaveisIds != null && !responsaveisIds.isEmpty()) {
                        if (tarefa.getResponsaveis() == null || tarefa.getResponsaveis().isEmpty()) {
                            return false;
                        }

                        boolean temResponsavel = tarefa.getResponsaveis().stream()
                                .anyMatch(resp -> responsaveisIds.contains(resp.getUsuId()));

                        if (!temResponsavel) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }
}