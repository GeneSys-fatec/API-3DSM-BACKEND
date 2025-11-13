package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto.BuscaProjetoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto.ExcluiProjetoService;

@RestController
@RequestMapping("/projeto")
public class ExcluiProjetoController {
    @Autowired
    private ExcluiProjetoService excluiProjetoService;

    @Autowired
    private BuscaProjetoService buscaProjetoService;

    @Autowired
    private PermissaoService permissaoService;

    @DeleteMapping("/apagar/{projId}")
    public ResponseEntity<String> apagarProjeto(
            @PathVariable String projId,
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {

        if (!permissaoService.podeAcessarProjeto(usuario.getUsuId(), projId)) {
            throw new AcessoNaoAutorizadoException("Acesso Negado",
                    "Você não tem permissão para apagar este projeto.");
        }

        ProjetoModel projeto = buscaProjetoService.buscarPorId(projId)
                .orElseThrow(() -> new ProjetoNaoEncontradoException("Projeto não encontrado",
                        "Não foi possível deletar o projeto com id " + projId + ", pois não foi encontrado"));

        excluiProjetoService.deletar(projId);
        return ResponseEntity.ok("Projeto apagado com sucesso");
    }
}