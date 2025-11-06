package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Tarefa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.AdicionadorLinkTarefa;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.TarefaConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa.BuscaTarefaService;

@RestController
@RequestMapping("/tarefa")
public class BuscaTarefaController {
    @Autowired
    private BuscaTarefaService buscaTarefaService;

    @Autowired
    private AdicionadorLinkTarefa adicionadorLink;

    @Autowired
    private TarefaConverter tarefaConverterService;


    @Autowired
    private PermissaoService permissaoService;


    @GetMapping("/listar-por-usuario")
    public ResponseEntity<List<TarefaDTO>> listarTarefasDoUsuario(
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {
    
        List<TarefaModel> tarefas = buscaTarefaService.listarTarefasPorResponsavel(usuarioId);

        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }



    @GetMapping("/{tarId}")
    public ResponseEntity<TarefaDTO> buscarPorId(
            @PathVariable String tarId,
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {
    
        if (!permissaoService.podeAcessarTarefa(usuarioId, tarId)) {
            throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para ver esta tarefa.");
        }

        Optional<TarefaModel> tarefaOpt = buscaTarefaService.buscarPorId(tarId);
        if (tarefaOpt.isPresent()) {
            TarefaDTO dto = tarefaConverterService.modelParaDto(tarefaOpt.get());
            adicionadorLink.adicionarLink(dto);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    @GetMapping("/por-projeto/{projId}")
    public ResponseEntity<List<TarefaDTO>> listarTarefasPorProjeto(
            @PathVariable String projId,
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {
    
        if (!permissaoService.podeAcessarProjeto(usuarioId, projId)) {
            throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para ver as tarefas deste projeto.");
        }

        List<TarefaModel> tarefas = buscaTarefaService.listarPorProjetoUnico(projId);
        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/listar")
    public ResponseEntity<List<TarefaDTO>> listarTarefa() {
    
        List<TarefaModel> tarefas = buscaTarefaService.listarTodas();
        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }





}