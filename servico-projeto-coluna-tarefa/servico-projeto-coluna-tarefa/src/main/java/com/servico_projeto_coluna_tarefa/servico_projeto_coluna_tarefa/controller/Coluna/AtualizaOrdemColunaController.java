package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; 
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 2. IMPORTE SEU DTO "ESPELHO"
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO; 
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ReordenarColunasDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
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
            @AuthenticationPrincipal UsuarioDTO usuario 
    ) {
    
        if (!permissaoService.podeAcessarProjeto(usuario.getUsuId(), dto.getProjetoId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    
        editaColunaService.atualizarOrdemColunas(dto.getProjetoId(), dto.getColunasIdsOrdenadas());
        return ResponseEntity.ok().build();
    }
}