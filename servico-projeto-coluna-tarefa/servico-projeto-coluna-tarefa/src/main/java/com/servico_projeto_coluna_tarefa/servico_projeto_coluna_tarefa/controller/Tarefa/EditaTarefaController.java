package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Tarefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// 1. IMPORTAÇÃO REAL
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader; // <-- REMOVIDO
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;
// 2. IMPORTE SEU DTO "ESPELHO"
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa.EditaTarefaService;

@RestController
@RequestMapping("/tarefa")
public class EditaTarefaController {
    @Autowired
    private EditaTarefaService editaTarefaService;

    @Autowired
    private PermissaoService permissaoService;

    @PutMapping("/atualizar/{tarId}")
    public ResponseEntity<String> atualizarTarefa(
            @PathVariable String tarId,
            @RequestBody TarefaDTO dto,
            // 3. INJETE O USUÁRIO REAL
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {
    
        if (dto.getTarTitulo() == null || dto.getTarTitulo().isBlank() ||
                dto.getTarDescricao() == null || dto.getTarDescricao().isBlank() ||
                dto.getTarPrazo() == null || dto.getTarPrazo().isBlank()) {

            throw new InvalidTaskDataException("Erro ao atualizar tarefa",
                    "Título, descrição e data são obrigatórios.");
        }
    
        // 4. USE O ID DE USUÁRIO REAL
        if (!permissaoService.podeAcessarTarefa(usuario.getUsuId(), tarId)) {
            throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para editar esta tarefa.");
        }
    
        try {
            editaTarefaService.atualizarTarefa(tarId, dto);
            return ResponseEntity.ok("Tarefa atualizada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}