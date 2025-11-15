package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${usuario.service.url}")
    private String usuarioServiceUrl;

    @Value("${notificacao.service.url}")
    private String notificacaoServiceUrl;

    @Value("${internal.api.key}")
    private String apiKey;

    @Bean
    public WebClient usuarioWebClient() {
        return WebClient.builder()
                .baseUrl(usuarioServiceUrl)
                .build();
    }

    @Bean
    public WebClient notificacaoWebClient() {
        return WebClient.builder()
                .baseUrl(notificacaoServiceUrl)
                .defaultHeader("X-Internal-API-Key", apiKey)
                .build();
    }
}