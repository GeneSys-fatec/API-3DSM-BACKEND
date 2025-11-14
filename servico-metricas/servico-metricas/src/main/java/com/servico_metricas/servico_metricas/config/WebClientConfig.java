package com.servico_metricas.servico_metricas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${internal.api.key}")
    private String apiKey;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8000")
                .defaultHeader("X-Internal-API-Key", apiKey)
                .build();
    }
}