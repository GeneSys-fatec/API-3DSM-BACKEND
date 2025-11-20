package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client;

// ... (imports)

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.NotificacaoRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NotificacaoClient {

    @Autowired
    @Qualifier("notificacaoWebClient")
    private WebClient webClient;

    // --- CORREÇÃO: Removido o parâmetro authHeader ---
    public void criarNotificacaoAtribuicao(NotificacaoRequestDTO notificacaoDTO) {
        webClient.post()
                .uri("/notificacao/criar/atribuicao")
                // --- CORREÇÃO: Removido o .header("Authorization", authHeader) ---
                .body(Mono.just(notificacaoDTO), NotificacaoRequestDTO.class)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> System.err.println("Erro ao enviar notificação: " + error.getMessage()))
                .subscribe();
    }
}