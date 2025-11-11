package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

// Importe seus repositórios locais (do projeto-service)
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ProjetoRepository;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ColunaModel; // (Você vai precisar deste)
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ColunaRepository; // (E deste)

import java.util.Optional;

@Service
public class PermissaoService {

    // --- Repositórios Locais (do projeto-service) ---
    @Autowired
    private TarefaRepository tarefaRepository;
    
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ColunaRepository colunaRepository; // (Certifique-se de que este @Autowired exista)

    // --- Cliente Web para o User Service ---
    @Autowired
    private WebClient userWebClient; // (Bean do seu AppConfig)

    /**
     * VERIFICAÇÃO PRINCIPAL: O usuário é membro da equipe dona do projeto?
     * Esta é a lógica central que os outros métodos irão reutilizar.
     */
    public boolean podeAcessarProjeto(String usuarioId, String projetoId) {
        if (projetoId == null || projetoId.isBlank() || usuarioId == null) {
            return false;
        }

        // 1. O projeto existe? A qual equipe ele pertence?
        Optional<ProjetoModel> projetoOpt = projetoRepository.findById(projetoId);
        if (projetoOpt.isEmpty()) {
            return false; // Projeto não existe
        }
        String equipeId = projetoOpt.get().getEquipeId();

        // 2. Chame o user-service para verificar a permissão
        try {
            // Chama GET /equipes/interno/validar-membro?usuarioId=...&equipeId=...
            Boolean temPermissao = userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/equipes/interno/validar-membro")
                    .queryParam("usuarioId", usuarioId)
                    .queryParam("equipeId", equipeId)
                    .build())
                .retrieve() // Executa
                .bodyToMono(Boolean.class) // Espera um 'true' ou 'false'
                .block(); // Torna a chamada síncrona

            return Boolean.TRUE.equals(temPermissao);

        } catch (Exception e) {
            // Se o user-service falhar (404, 500, etc.), negue o acesso.
            System.err.println("Erro ao validar permissão (podeAcessarProjeto): " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se um usuário pode acessar uma tarefa.
     * (Reutiliza a lógica principal)
     */
    public boolean podeAcessarTarefa(String usuarioId, String tarefaId) {
        if (tarefaId == null || usuarioId == null) return false;

        Optional<TarefaModel> tarefaOpt = tarefaRepository.findById(tarefaId);
        if (tarefaOpt.isEmpty()) {
            return false; // Tarefa não existe
        }
        
        // Encontra o projeto da tarefa e usa a lógica principal
        String projId = tarefaOpt.get().getProjId();
        return podeAcessarProjeto(usuarioId, projId);
    }

    /**
     * Verifica se um usuário pode modificar uma coluna.
     * (Reutiliza a lógica principal)
     */
    public boolean podeModificarColuna(String usuarioId, String colunaId) {
        if (colunaId == null || usuarioId == null) return false;

        // 1. Busca a coluna no banco local
        Optional<ColunaModel> colunaOpt = colunaRepository.findById(colunaId);
        if (colunaOpt.isEmpty()) {
            return false; // Coluna não existe
        }

        // 2. Encontra o projeto da coluna e usa a lógica principal
        String projId = colunaOpt.get().getProjId();
        return podeAcessarProjeto(usuarioId, projId);
    }

    /**
     * Verifica se um usuário pode criar projetos em uma equipe (se ele é membro).
     */
    public boolean podeCriarProjetosNaEquipe(String usuarioId, String equipeId) {
        if (equipeId == null || equipeId.isBlank() || usuarioId == null) {
            return false;
        }

        try {
            Boolean temPermissao = userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/equipes/interno/validar-membro")
                    .queryParam("usuarioId", usuarioId)
                    .queryParam("equipeId", equipeId)
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