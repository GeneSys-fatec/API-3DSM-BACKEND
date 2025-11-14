package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ColunaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
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
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {
    
        if (!permissaoService.podeModificarColuna(usuario.getUsuId(), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    
        ColunaDTO colunaAtualizada = editaColunaService.atualizarColuna(id, colunaDTO);
        return ResponseEntity.ok(colunaAtualizada);
    }
}