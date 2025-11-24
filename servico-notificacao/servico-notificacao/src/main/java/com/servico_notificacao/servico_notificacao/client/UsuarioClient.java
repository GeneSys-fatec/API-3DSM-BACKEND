package com.servico_notificacao.servico_notificacao.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.servico_notificacao.servico_notificacao.model.dto.UsuarioDTO;

import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(@Value("${usuario.service.url}") String usuarioServiceUrl,
                         @Value("${internal.api.key}") String apiKey,
                         WebClient.Builder builder) {

        this.webClient = builder
                .baseUrl(usuarioServiceUrl)
                .defaultHeader("X-Internal-API-Key", apiKey)
                .build();
    }


    public Mono<UsuarioDTO> getUsuarioMono(String usuId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/usuario/" + usuId).build())
                .retrieve()
                .bodyToMono(UsuarioDTO.class);
    }

    public UsuarioDTO getUsuario(String usuId) {
        return getUsuarioMono(usuId).onErrorResume(ex -> Mono.empty()).block();
    }


    public Mono<UsuarioDTO> getUsuarioSessaoMono(String token) {
        return webClient.get()
                .uri("/auth/session")
                .cookie("jwt-token", token)
                .retrieve()
                .bodyToMono(UsuarioDTO.class)
                .onErrorResume(ex -> Mono.empty());
    }

    public UsuarioDTO getUsuarioSessao(String token) {
        return getUsuarioSessaoMono(token).block();
    }
}
