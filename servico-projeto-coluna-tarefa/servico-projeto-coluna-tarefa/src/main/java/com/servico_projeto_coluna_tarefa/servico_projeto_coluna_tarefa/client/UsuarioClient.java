package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;

import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {

    @Autowired
    @Qualifier("usuarioWebClient")
    private WebClient webClient;


    public UsuarioDTO getUsuarioSessao(String authHeader) {
        try {
            return webClient.get()
                    .uri("/auth/session")
                    .header("Authorization", authHeader)
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Erro ao buscar sessão do usuário: " + e.getMessage());
            return null;
        }
    }
}