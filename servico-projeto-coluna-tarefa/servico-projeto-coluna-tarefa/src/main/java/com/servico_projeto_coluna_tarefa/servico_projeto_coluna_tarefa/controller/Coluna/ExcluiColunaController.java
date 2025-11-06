package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Coluna.ExcluiColunaService;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;

@RestController
@RequestMapping("/colunas")
public class ExcluiColunaController {

    @Autowired
    private ExcluiColunaService excluiColunaService;

    @Autowired
    private PermissaoService permissaoService;

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarColuna(@PathVariable String id, @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId) {

        if (!permissaoService.podeModificarColuna(usuarioId, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        excluiColunaService.deletarColuna(id);

        return ResponseEntity.noContent().build();
    }
}