package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ProjetoDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto.BuscaProjetoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto.EditaProjetoService;
;


@RestController
@RequestMapping("/projeto")
public class EditaProjetoController {
    @Autowired
    private EditaProjetoService editaProjetoService;

    @Autowired
    private BuscaProjetoService buscaProjetoService;

    @Autowired
    private PermissaoService permissaoService;

    @PutMapping("/atualizar/{projId}")
    public ResponseEntity<String> atualizar(
            @PathVariable String projId,
            @RequestBody ProjetoDTO dto,
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {

        if (!permissaoService.podeAcessarProjeto(usuario.getUsuId(), projId)) {
            throw new AcessoNaoAutorizadoException("Acesso Negado",
                    "Você não tem permissão para editar este projeto.");
        }
    
        ProjetoModel projeto = buscaProjetoService.buscarPorId(projId)
                .orElseThrow(() -> new ProjetoNaoEncontradoException("Projeto não encontrado",
                        "Projeto com id " + projId + " não foi encontrado"));

        projeto.setProjNome(dto.getProjNome());
        projeto.setProjDescricao(dto.getProjDescricao());
        projeto.setProjStatus(dto.getProjStatus());

        editaProjetoService.salvar(projeto);
        return ResponseEntity.ok("Projeto atualizado com sucesso");
    }
}