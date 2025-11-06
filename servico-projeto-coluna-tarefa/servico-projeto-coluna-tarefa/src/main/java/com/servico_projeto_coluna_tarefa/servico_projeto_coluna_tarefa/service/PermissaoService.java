package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;
import java.util.Optional;

@Service
public class PermissaoService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public boolean podeAcessarProjeto(String usuarioId, String projetoId) {
        System.out.println("VERIFICANDO PERMISSÃO (MOCK): Usuário " + usuarioId + " no Projeto " + projetoId);
        if (projetoId == null || projetoId.isBlank()) {
            return false;
        }
        return true;
    }

    public boolean podeModificarColuna(String usuarioId, String colunaId) {
        System.out.println("VERIFICANDO PERMISSÃO (MOCK): Usuário " + usuarioId + " na Coluna " + colunaId);
        return true;
    }

    public boolean podeCriarProjetosNaEquipe(String usuarioId, String equipeId) {
        System.out.println("VERIFICANDO PERMISSÃO (MOCK): Usuário " + usuarioId + " pode criar na Equipe " + equipeId);
        if (equipeId == null || equipeId.isBlank()) {
            return false;
        }
        return true;
    }

    public boolean podeAcessarTarefa(String usuarioId, String tarefaId) {
        System.out.println("VERIFICANDO PERMISSÃO (MOCK): Usuário " + usuarioId + " na Tarefa " + tarefaId);
        if (tarefaId == null) return false;

        Optional<TarefaModel> tarefaOpt = tarefaRepository.findById(tarefaId);
        if (tarefaOpt.isEmpty()) {
            return false;
        }
        String projId = tarefaOpt.get().getProjId();

        return podeAcessarProjeto(usuarioId, projId);
    }
}