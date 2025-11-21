package com.servico_auditoria.servico_auditoria.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.servico_auditoria.servico_auditoria.model.dto.UsuarioDTO;

@Component
public class UsuarioClient {

    @Autowired
    private WebClient webClient;


    public UsuarioDTO getUsuarioSessao(String token) {
        try {
            return webClient.get()
                    .uri("/auth/session")
                    .cookie("jwt-token", token)
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Erro ao buscar sessão do usuário: " + e.getMessage());
            return null;
        }
    }
}