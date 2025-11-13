package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ColunaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Coluna.BuscaColunaService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;

@RestController
@RequestMapping("/colunas")
public class BuscaColunaController {
    @Autowired
    private BuscaColunaService buscaColunaService;

    @Autowired
    private PermissaoService permissaoService;

    @GetMapping("/por-projeto/{projId}")
    public ResponseEntity<List<ColunaDTO>> listarColunasPorProjeto(
            @PathVariable String projId,
            @AuthenticationPrincipal UsuarioDTO usuario 
    ) {
    
        if (!permissaoService.podeAcessarProjeto(usuario.getUsuId(), projId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    
        return ResponseEntity.ok(buscaColunaService.listarPorProjeto(projId));
    }
}