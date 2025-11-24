package com.servico_auditoria.servico_auditoria.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${usuario.service.base-url:http://localhost:8080}")
    private String usuarioServiceBaseUrl;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(usuarioServiceBaseUrl).build();
    }
}
