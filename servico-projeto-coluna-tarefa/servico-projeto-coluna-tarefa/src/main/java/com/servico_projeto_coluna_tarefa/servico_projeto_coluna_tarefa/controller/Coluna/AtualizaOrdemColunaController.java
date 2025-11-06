package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ReordenarColunasDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Coluna.EditaColunaService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;

@RestController
@RequestMapping("/colunas")
public class AtualizaOrdemColunaController {

    @Autowired
    private EditaColunaService editaColunaService;

    @Autowired
    private PermissaoService permissaoService;

    @PutMapping("/reordenar")
    public ResponseEntity<Void> reordenarColunas(
            @RequestBody ReordenarColunasDTO dto,
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {
    
        if (!permissaoService.podeAcessarProjeto(usuarioId, dto.getProjetoId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    

    
        editaColunaService.atualizarOrdemColunas(dto.getProjetoId(), dto.getColunasIdsOrdenadas());
        return ResponseEntity.ok().build();
    }
}