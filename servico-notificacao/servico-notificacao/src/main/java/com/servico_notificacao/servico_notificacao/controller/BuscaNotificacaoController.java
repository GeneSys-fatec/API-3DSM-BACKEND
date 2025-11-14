package com.servico_notificacao.servico_notificacao.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// Removido AuthenticationPrincipal: agora buscamos usuário via /auth/session.
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {

        // Normaliza Authorization: trata casos como "Bearer", "Bearer ", "Bearer null/undefined"
        String authHeader = authorization;
        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        if (authHeader.startsWith("Bearer")) {
            String token = authHeader.length() > 6 ? authHeader.substring(6).trim() : "";
            if (token.isEmpty() || token.equalsIgnoreCase("null") || token.equalsIgnoreCase("undefined")) {
                return ResponseEntity.ok(List.of());
            }
        }

        UsuarioDTO usuarioLogado = usuarioClient.getUsuarioSessao(authHeader);
        if (usuarioLogado == null || usuarioLogado.getUsuId() == null) {
            // Header presente mas sessão inválida -> 401 para o front tratar.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    List<NotificacaoDTO> notificacoes = buscaNotificacaoService
        .listarPorUsuario(usuarioLogado.getUsuId())
        .stream()
        .map(NotificacaoConverter::modelParaDto)
        .collect(Collectors.toList());

        return ResponseEntity.ok(notificacoes);
    }
}
