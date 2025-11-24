package com.servico_notificacao.servico_notificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.servico_notificacao.servico_notificacao.service.ExcluiNotificacaoService;

@RestController
@RequestMapping("/notificacao")

public class DeletaNotificacaoController {

    @Autowired
    private ExcluiNotificacaoService excluiNotificacaoService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarNotificacao(@PathVariable String id) {
        try {
            excluiNotificacaoService.deletarNotificacao(id);
            return ResponseEntity.noContent().build(); 
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); 
        }
    }
}
