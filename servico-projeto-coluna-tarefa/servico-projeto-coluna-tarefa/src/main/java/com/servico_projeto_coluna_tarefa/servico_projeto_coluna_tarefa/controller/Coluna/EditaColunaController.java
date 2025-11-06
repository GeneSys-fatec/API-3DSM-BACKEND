package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ColunaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Coluna.EditaColunaService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/colunas")
public class EditaColunaController {
    @Autowired
    private EditaColunaService editaColunaService;

    @Autowired
    private PermissaoService permissaoService;

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ColunaDTO> atualizarColuna(
            @PathVariable String id,
            @Valid @RequestBody ColunaDTO colunaDTO,
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {
    
        if (!permissaoService.podeModificarColuna(usuarioId, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    

        ColunaDTO colunaAtualizada = editaColunaService.atualizarColuna(id, colunaDTO);
        return ResponseEntity.ok(colunaAtualizada);
    }
}