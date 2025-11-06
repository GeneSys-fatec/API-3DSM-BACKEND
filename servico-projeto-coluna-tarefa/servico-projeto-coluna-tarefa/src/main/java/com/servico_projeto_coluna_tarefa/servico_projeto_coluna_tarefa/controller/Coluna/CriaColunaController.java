package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.ColunaConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ColunaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ColunaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Coluna.CriaColunaService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.PermissaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/colunas")
public class CriaColunaController {
    @Autowired
    private CriaColunaService criaColunaService;

    @Autowired
    private ColunaConverter colunaConverter;

    @Autowired
    private PermissaoService permissaoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ColunaDTO> criarColuna(
            @Valid @RequestBody ColunaDTO colunaDTO,
            @RequestHeader(value="X-User-ID", defaultValue="ID-DE-TESTE") String usuarioId
    ) {
    
    
        String projetoId = colunaDTO.getProjId();
        if (!permissaoService.podeAcessarProjeto(usuarioId, projetoId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    

        ColunaModel colunaParaSalvar = colunaConverter.dtoParaModel(colunaDTO);
        ColunaModel colunaSalva = criaColunaService.criarColuna(colunaParaSalvar);
        ColunaDTO dtoDeResposta = colunaConverter.modelParaDto(colunaSalva);
        return new ResponseEntity<>(dtoDeResposta, HttpStatus.CREATED);
    }
}