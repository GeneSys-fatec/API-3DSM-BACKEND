package com.servico_usuario_equipe.servico_usuario_equipe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${projeto.service.url}")
    private String projetoServiceUrl;

    @Bean
    public WebClient projetoWebClient() {
        return WebClient.builder()
            .baseUrl(projetoServiceUrl)
            .build();
    }
}
