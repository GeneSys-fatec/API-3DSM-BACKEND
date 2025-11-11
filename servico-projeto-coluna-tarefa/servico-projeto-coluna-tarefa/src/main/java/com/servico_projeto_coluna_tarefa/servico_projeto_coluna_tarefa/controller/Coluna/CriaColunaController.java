package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Coluna;

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

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.ColunaConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ColunaDTO;
// 2. IMPORTE SEU DTO "ESPELHO"
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO; 
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
            // 3. INJETE O USUÁRIO REAL
            @AuthenticationPrincipal UsuarioDTO usuario 
    ) {
    
        String projetoId = colunaDTO.getProjId();
        // 4. USE O ID DE USUÁRIO REAL
        if (!permissaoService.podeAcessarProjeto(usuario.getUsuId(), projetoId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    
        ColunaModel colunaParaSalvar = colunaConverter.dtoParaModel(colunaDTO);
        ColunaModel colunaSalva = criaColunaService.criarColuna(colunaParaSalvar);
        ColunaDTO dtoDeResposta = colunaConverter.modelParaDto(colunaSalva);
        return new ResponseEntity<>(dtoDeResposta, HttpStatus.CREATED);
    }
}