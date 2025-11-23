package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuditoriaClient {

    @Autowired
    @Qualifier("auditoriaWebClient")
    private WebClient webClient;

    public void registrarLog(RegistrarLogRequest request, String userId, String userEmail, String userName) {
        webClient.post()
                .uri("/auditoria/logs")
                .header("X-User-Id", userId != null ? userId : "sistema")
                .header("X-User-Email", userEmail != null ? userEmail : "sistema@email.com")
                .header("X-User-Name", userName != null ? userName : "Sistema")
                .body(Mono.just(request), RegistrarLogRequest.class)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> System.err.println("[AuditoriaClient] Erro ao registrar log: " + error.getMessage()))
                .subscribe();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistrarLogRequest {
        private String projetoId;
        private String tarefaId;
        private String tarefaNome;
        private String responsavelId;
        private String responsavelEmail;
        private List<ModificacaoSimplesDTO> modificacoes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModificacaoSimplesDTO {
        private String categoria;
        private String modificacao;
    }
}