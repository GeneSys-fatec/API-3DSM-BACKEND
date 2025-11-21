package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client.AuditoriaClient;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;

import java.util.List;

@Service
public class ExcluiTarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private AuditoriaClient auditoriaClient;

    public void deletar(String id, String usuarioId, String usuarioEmail, String usuarioNome) {

        TarefaModel tarefa = tarefaRepository.findById(id).orElse(null);

        if (tarefa != null) {
            try {
                AuditoriaClient.RegistrarLogRequest log = new AuditoriaClient.RegistrarLogRequest(
                        tarefa.getProjId(),
                        tarefa.getTarId(),
                        tarefa.getTarTitulo(),
                        usuarioId,
                        usuarioEmail,
                        List.of(new AuditoriaClient.ModificacaoSimplesDTO("EXCLUSAO", "Tarefa excluída permanentemente"))
                );

                auditoriaClient.registrarLog(log, usuarioId, usuarioEmail, usuarioNome);
            } catch (Exception e) {
                System.err.println("Erro ao registrar auditoria de exclusão: " + e.getMessage());
            }
        }

        tarefaRepository.deleteById(id);
    }
}