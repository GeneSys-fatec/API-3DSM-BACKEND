package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto;


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
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoSemInformacaoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.ProjetoConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ProjetoDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto.CriaProjetoService;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
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
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {

        if (dto.getProjNome() == null || dto.getProjNome().isBlank()) {
            throw new ProjetoSemInformacaoException("Erro ao cadastrar projeto", "Nome do projeto é obrigatório.");
        }

    
    
        String equipeId = dto.getEquId();

        if (equipeId == null || equipeId.isBlank()) {
        
            throw new ProjetoSemInformacaoException("Erro ao cadastrar projeto", "A equipe do projeto (equId) é obrigatória.");
        }
    

        if (!permissaoService.podeCriarProjetosNaEquipe(usuarioId, equipeId)) {
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