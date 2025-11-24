package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Tarefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader; // <-- REMOVIDO
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.TarefaConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService; // <-- IMPORTADO
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa.CriaTarefaService;

@RestController
@RequestMapping("/tarefa")
public class CriaTarefaController {
    @Autowired
    private CriaTarefaService criaTarefaService;

    @Autowired
    private TarefaConverter tarefaConverterService;

    // 1. INJETE O PERMISSAOSERVICE
    @Autowired
    private PermissaoService permissaoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarTarefa(
            @RequestBody TarefaDTO dto,
            @AuthenticationPrincipal UsuarioDTO usuarioLogado // <-- JÁ ESTAVA CORRETO
    ) {

        if (dto.getTarTitulo() == null || dto.getTarTitulo().isBlank() ||
            dto.getTarDescricao() == null || dto.getTarDescricao().isBlank() ||
            dto.getTarPrazo() == null || dto.getTarPrazo().isBlank()) {

            throw new InvalidTaskDataException("Erro ao cadastrar tarefa",
                    "Título, descrição e data são obrigatórios.");
        }
        
        // 2. ADICIONE A VERIFICAÇÃO DE PERMISSÃO
        if (!permissaoService.podeAcessarProjeto(usuarioLogado.getUsuId(), dto.getProjId())) {
             throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para criar tarefas neste projeto.");
        }
        
        // O seu "criarTarefa" já pode receber o DTO e o Usuário
        TarefaModel salva = criaTarefaService.criarTarefa(dto, usuarioLogado);
        TarefaDTO dtoDeResposta = tarefaConverterService.modelParaDto(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoDeResposta);
    }
}