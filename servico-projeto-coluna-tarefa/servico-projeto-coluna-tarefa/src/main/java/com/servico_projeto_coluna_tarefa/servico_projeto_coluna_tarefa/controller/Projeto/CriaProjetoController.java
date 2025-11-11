package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// 1. IMPORTAÇÃO REAL
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader; // <-- REMOVIDO
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoSemInformacaoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.ProjetoConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ProjetoDTO;
// 2. IMPORTE SEU DTO "ESPELHO"
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto.CriaProjetoService;

@RestController
@RequestMapping("/projeto")
public class CriaProjetoController {
    @Autowired
    private CriaProjetoService criaProjetoService;
    @Autowired
    private ProjetoConverter projetoConverter;

    @Autowired
    private PermissaoService permissaoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarProjeto(
            @RequestBody ProjetoDTO dto,
            // 3. INJETE O USUÁRIO REAL
            @AuthenticationPrincipal UsuarioDTO usuario
    ) {

        if (dto.getProjNome() == null || dto.getProjNome().isBlank()) {
            throw new ProjetoSemInformacaoException("Erro ao cadastrar projeto", "Nome do projeto é obrigatório.");
        }
    
        String equipeId = dto.getEquId();
        if (equipeId == null || equipeId.isBlank()) {
            throw new ProjetoSemInformacaoException("Erro ao cadastrar projeto", "A equipe do projeto (equId) é obrigatória.");
        }
    
        // 4. USE O ID DE USUÁRIO REAL
        if (!permissaoService.podeCriarProjetosNaEquipe(usuario.getUsuId(), equipeId)) {
            throw new AcessoNaoAutorizadoException("Acesso Negado",
                    "Você não tem permissão para criar projetos nesta equipe.");
        }

        ProjetoModel projeto = projetoConverter.dtoParaModel(dto);
        criaProjetoService.criarNovoProjeto(projeto, equipeId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Projeto cadastrado com sucesso!");
    }
}