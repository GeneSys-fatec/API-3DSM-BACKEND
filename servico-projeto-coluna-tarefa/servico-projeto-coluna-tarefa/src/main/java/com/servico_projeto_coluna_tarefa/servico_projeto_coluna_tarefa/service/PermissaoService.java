package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ColunaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ColunaRepository;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ProjetoRepository;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;

@Service
public class PermissaoService {

    @Autowired
    private TarefaRepository tarefaRepository;
    
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ColunaRepository colunaRepository;

    @Autowired
    @Qualifier("usuarioWebClient")
    private WebClient userWebClient;

    public boolean podeAcessarProjeto(String usuId, String projetoId) {
        if (projetoId == null || projetoId.isBlank() || usuId == null) {
            return false;
        }

        Optional<ProjetoModel> projetoOpt = projetoRepository.findById(projetoId);
        if (projetoOpt.isEmpty()) {
            return false;
        }
        String equId = projetoOpt.get().getEquId();

        try {
            Boolean temPermissao = userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/equipe/interno/validar-membro")
                    .queryParam("usuId", usuId)
                    .queryParam("equId", equId)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

            return Boolean.TRUE.equals(temPermissao);

        } catch (Exception e) {
            System.err.println("Erro ao validar permissão (podeAcessarProjeto): " + e.getMessage());
            return false;
        }
    }

    public boolean podeAcessarTarefa(String usuId, String tarefaId) {
        if (tarefaId == null || usuId == null) return false;

        Optional<TarefaModel> tarefaOpt = tarefaRepository.findById(tarefaId);
        if (tarefaOpt.isEmpty()) {
            return false;
        }
        
        String projId = tarefaOpt.get().getProjId();
        return podeAcessarProjeto(usuId, projId);
    }

    public boolean podeModificarColuna(String usuId, String colunaId) {
        if (colunaId == null || usuId == null) return false;

        Optional<ColunaModel> colunaOpt = colunaRepository.findById(colunaId);
        if (colunaOpt.isEmpty()) {
            return false;
        }

        String projId = colunaOpt.get().getProjId();
        return podeAcessarProjeto(usuId, projId);
    }

    public boolean podeCriarProjetosNaEquipe(String usuId, String equId) {
        if (equId == null || equId.isBlank() || usuId == null) {
            return false;
        }

        try {
            Boolean temPermissao = userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/equipe/interno/validar-membro")
                    .queryParam("usuId", usuId)
                    .queryParam("equId", equId)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

            return Boolean.TRUE.equals(temPermissao);

        } catch (Exception e) {
            System.err.println("Erro ao validar permissão (podeCriarProjetosNaEquipe): " + e.getMessage());
            return false;
        }
    }
}