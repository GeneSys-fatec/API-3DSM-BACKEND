package com.servico_notificacao.servico_notificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CookieValue;

import com.servico_notificacao.servico_notificacao.model.dto.UsuarioDTO;

import org.springframework.http.HttpStatus;

import com.servico_notificacao.servico_notificacao.service.MarcaComoLidaNotificacaoService;
import com.servico_notificacao.servico_notificacao.client.UsuarioClient;


@RestController
@RequestMapping("/notificacao")

public class AtualizaNotificacaoController {

    @Autowired
    private MarcaComoLidaNotificacaoService marcaComoLidaNotificacaoService;

    @Autowired
    private UsuarioClient usuarioClient;

    @PutMapping("/marcar-lida/{id}")
    public ResponseEntity<Void> marcarComoLida(@PathVariable String id,
                                               @CookieValue(name = "jwt-token", required = false) String token) {

        String authHeader = null;
        if (token != null && !token.isBlank()) {
            authHeader = "Bearer " + token;
        }

        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.noContent().build();
        }

        try {
            UsuarioDTO usuario = usuarioClient.getUsuarioSessao(authHeader);
            if (usuario == null || usuario.getUsuId() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }

        marcaComoLidaNotificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/marcar-todas")
    public ResponseEntity<Void> marcarTodasComoLidas(
            @CookieValue(name = "jwt-token", required = false) String token) {

        String authHeader = null;
        if (token != null && !token.isBlank()) {
            authHeader = "Bearer " + token;
        }

        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.noContent().build();
        }

        UsuarioDTO usuario;
        try {
            usuario = usuarioClient.getUsuarioSessao(authHeader);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (usuario == null || usuario.getUsuId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        marcaComoLidaNotificacaoService.marcarTodasComoLidas(usuario.getUsuId());
        return ResponseEntity.ok().build();
    }
}