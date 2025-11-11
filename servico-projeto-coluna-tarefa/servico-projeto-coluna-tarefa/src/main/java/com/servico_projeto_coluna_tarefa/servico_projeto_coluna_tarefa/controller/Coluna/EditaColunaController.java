package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// 1. IMPORTAÇÃO REAL
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 2. IMPORTE SEU DTO "ESPELHO"
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
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
            // 3. INJETE O USUÁRIO REAL
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {
    
        // 4. USE O ID DE USUÁRIO REAL
        if (!permissaoService.podeModificarColuna(usuario.getUsuId(), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    
        ColunaDTO colunaAtualizada = editaColunaService.atualizarColuna(id, colunaDTO);
        return ResponseEntity.ok(colunaAtualizada);
    }
}