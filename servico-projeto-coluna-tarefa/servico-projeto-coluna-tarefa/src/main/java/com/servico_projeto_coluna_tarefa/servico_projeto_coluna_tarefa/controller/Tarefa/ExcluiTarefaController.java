package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Tarefa;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa.BuscaTarefaService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa.ExcluiTarefaService;

@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
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
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {
        if (!permissaoService.podeAcessarTarefa(usuarioId, tarId)) {
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