package com.servico_notificacao.servico_notificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.servico_notificacao.servico_notificacao.model.dto.UsuarioDTO;

// AuthenticationPrincipal removido: iremos resolver via chamada /auth/session.
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Void> marcarComoLida(@PathVariable String id) {
        marcaComoLidaNotificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/marcar-todas")
    public ResponseEntity<Void> marcarTodasComoLidas(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        UsuarioDTO usuario = usuarioClient.getUsuarioSessao(authorization);
        if (usuario == null || usuario.getUsuId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        marcaComoLidaNotificacaoService.marcarTodasComoLidas(usuario.getUsuId());
        return ResponseEntity.ok().build();
    }
}
