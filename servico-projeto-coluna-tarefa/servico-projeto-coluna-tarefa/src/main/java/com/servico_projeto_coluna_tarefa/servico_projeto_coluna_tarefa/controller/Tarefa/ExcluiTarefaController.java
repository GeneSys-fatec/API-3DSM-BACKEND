package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Tarefa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// 1. IMPORTAÇÃO REAL
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestHeader; // <-- REMOVIDO
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
// 2. IMPORTE SEU DTO "ESPELHO"
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa.BuscaTarefaService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa.ExcluiTarefaService;

@RestController
@RequestMapping("/tarefa")
public class ExcluiTarefaController {
    @Autowired
    private BuscaTarefaService buscaTarefaService;

    @Autowired
    private ExcluiTarefaService excluiTarefaService;

    @Autowired
    private PermissaoService permissaoService;

    @DeleteMapping("/apagar/{tarId}")
    public ResponseEntity<String> apagarTarefa(
            @PathVariable String tarId,
            // 3. INJETE O USUÁRIO REAL
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {
        // 4. USE O ID DE USUÁRIO REAL
        if (!permissaoService.podeAcessarTarefa(usuario.getUsuId(), tarId)) {
            throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para apagar esta tarefa.");
        }

        Optional<TarefaModel> tarefaExistente = buscaTarefaService.buscarPorId(tarId);
        if (tarefaExistente.isPresent()) {
            excluiTarefaService.deletar(tarId);
            return ResponseEntity.ok("Tarefa apagada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }
    }
}