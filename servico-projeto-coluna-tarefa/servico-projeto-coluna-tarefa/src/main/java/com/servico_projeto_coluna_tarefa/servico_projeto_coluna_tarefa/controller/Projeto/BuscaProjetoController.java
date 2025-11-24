package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.AdicionadorLinkProjetos;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.ProjetoConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ProjetoDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto.BuscaProjetoService;


@RestController
@RequestMapping("/projeto")
public class BuscaProjetoController {
    @Autowired
    private BuscaProjetoService buscaProjetoService;
    @Autowired
    private AdicionadorLinkProjetos adicionadorLink;
    @Autowired
    private ProjetoConverter projetoConverterService;

    @Autowired
    private PermissaoService permissaoService;

    @GetMapping("/meus-projetos")
    public ResponseEntity<List<ProjetoDTO>> listarProjetosDoUsuario(
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {
        List<ProjetoModel> projetos = buscaProjetoService.listarPorUsuario(usuario.getUsuId());
        
        List<ProjetoDTO> dtos = projetos.stream()
                .map(projetoConverterService::modelParaDto)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{projId}")
    public ResponseEntity<ProjetoDTO> buscarPorId(
            @PathVariable String projId,
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {
        if (!permissaoService.podeAcessarProjeto(usuario.getUsuId(), projId)) {
            throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para ver este projeto.");
        }

        ProjetoModel projeto = buscaProjetoService.buscarPorId(projId)
                .orElseThrow(() -> new ProjetoNaoEncontradoException(
                        "Projeto não encontrado",
                        "Projeto com id " + projId + " não foi encontrado"));

        ProjetoDTO dto = projetoConverterService.modelParaDto(projeto);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/listar")
    public ResponseEntity<List<ProjetoDTO>> listarTodas() {
        List<ProjetoModel> projetos = buscaProjetoService.listarTodas();
        List<ProjetoDTO> dtos = projetos.stream().map(projetoConverterService::modelParaDto)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/listar-por-id")
    public ResponseEntity<List<ProjetoDTO>> getProjetosByIds(
            @RequestParam("ids") List<String> ids) {
        List<ProjetoDTO> projetos = buscaProjetoService.buscarPorListaDeIds(ids);
        return ResponseEntity.ok(projetos);
    }

    @ExceptionHandler(ProjetoNaoEncontradoException.class)
    public ResponseEntity<String> handleProjetoNaoEncontrado(ProjetoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMensagem());
    }
}