package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Tarefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.TarefaConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa.CriaTarefaService;

@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
public class CriaTarefaController {
    @Autowired
    private CriaTarefaService criaTarefaService;

    @Autowired
    private TarefaConverter tarefaConverterService;

    @Autowired
    private PermissaoService permissaoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarTarefa(
            @RequestBody TarefaDTO dto,
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {
        if (dto.getTarTitulo() == null || dto.getTarTitulo().isBlank() ||
                dto.getTarDescricao() == null || dto.getTarDescricao().isBlank() ||
                dto.getTarPrazo() == null || dto.getTarPrazo().isBlank()) {

            throw new InvalidTaskDataException("Erro ao cadastrar tarefa",
                    "Título, descrição e data são obrigatórios.");
        }

        if (!permissaoService.podeAcessarProjeto(usuarioId, dto.getProjId())) {
            throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para criar tarefas neste projeto.");
        }

        TarefaModel salva = criaTarefaService.criarTarefa(dto);

        TarefaDTO dtoDeResposta = tarefaConverterService.modelParaDto(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoDeResposta);
    }
}