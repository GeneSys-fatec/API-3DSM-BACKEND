package com.servico_notificacao.servico_notificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.servico_notificacao.servico_notificacao.model.dto.NotificacaoRequestDTO;
import com.servico_notificacao.servico_notificacao.service.CriaNotificacaoService;
import com.servico_notificacao.servico_notificacao.model.NotificacaoModel;

@RestController
@RequestMapping("/notificacao")
public class CriaNotificacaoController {

    @Autowired
    private CriaNotificacaoService criaNotificacaoService;

    @PostMapping("/criar/atribuicao")
    public ResponseEntity<NotificacaoModel> criarNotificacaoAtribuicao(
            @RequestBody NotificacaoRequestDTO dto) {

        System.out.println("\n[CriaNotificacaoController] RECEBIDA CHAMADA PARA CRIAR NOTIFICAÇÃO!");
        System.out.println("[CriaNotificacaoController] De: " + dto.getNomeCriador());
        System.out.println("[CriaNotificacaoController] Para: " + dto.getUsuarioDestinoId());
        System.out.println("[CriaNotificacaoController] Tarefa: " + dto.getNomeTarefa());

        NotificacaoModel notificacao = criaNotificacaoService.criarNotificacaoAtribuicao(
                dto.getUsuarioOrigemId(),
                dto.getUsuarioDestinoId(),
                dto.getNomeCriador(),
                dto.getTarefaId(),
                dto.getNomeTarefa()
        );

        return ResponseEntity.ok(notificacao);
    }
}