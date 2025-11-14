package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${usuario.service.url}")
    private String projetoServiceUrl;

    @Bean
    public WebClient usuarioWebClient() {
        return WebClient.builder()
            .baseUrl(projetoServiceUrl)
            .build();
    }
}
