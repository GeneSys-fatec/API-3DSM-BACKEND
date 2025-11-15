package com.servico_notificacao.servico_notificacao.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientException;

import com.servico_notificacao.servico_notificacao.model.converter.NotificacaoConverter;
import com.servico_notificacao.servico_notificacao.model.dto.NotificacaoDTO;
import com.servico_notificacao.servico_notificacao.model.dto.UsuarioDTO;
import com.servico_notificacao.servico_notificacao.client.UsuarioClient;
import com.servico_notificacao.servico_notificacao.service.BuscaNotificacaoService;

@RestController
@RequestMapping("/notificacao")

public class BuscaNotificacaoController {

    @Autowired
    private BuscaNotificacaoService buscaNotificacaoService;

    @Autowired
    private UsuarioClient usuarioClient;

    @GetMapping("/listar")
    public ResponseEntity<List<NotificacaoDTO>> listarPorUsuario(
            @CookieValue(name = "jwt-token", required = false) String token) {

        System.out.println("\n--- DEBUG [CONTROLLER TOPO] ---");
        System.out.println("Cookie 'jwt-token' recebido: '" + token + "'");
        System.out.println("---------------------------------\n");

        String authHeader = null;
        if (token != null && !token.isBlank()) {
            authHeader = "Bearer " + token;
        }

        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.ok(List.of());
        }

        if (authHeader.startsWith("Bearer")) {
            String tokenValue = authHeader.length() > 6 ? authHeader.substring(6).trim() : "";
            if (tokenValue.isEmpty() || tokenValue.equalsIgnoreCase("null") || tokenValue.equalsIgnoreCase("undefined")) {
                return ResponseEntity.ok(List.of());
            }
        }

        UsuarioDTO usuarioLogado;
        try {
            usuarioLogado = usuarioClient.getUsuarioSessao(authHeader);

        } catch (WebClientException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (usuarioLogado == null || usuarioLogado.getUsuId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("\n--- DEBUG NOTIFICAÇÃO [CONTROLLER] ---");
        System.out.println("ID do usuário recebido do UsuarioClient: " + usuarioLogado.getUsuId());
        System.out.println("----------------------------------------\n");

        List<NotificacaoDTO> notificacoes = buscaNotificacaoService
                .listarPorUsuario(usuarioLogado.getUsuId())
                .stream()
                .map(NotificacaoConverter::modelParaDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(notificacoes);
    }
}