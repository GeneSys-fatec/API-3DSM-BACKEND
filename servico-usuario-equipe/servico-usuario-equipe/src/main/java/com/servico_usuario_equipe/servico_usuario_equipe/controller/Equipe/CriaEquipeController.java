package com.servico_usuario_equipe.servico_usuario_equipe.controller.Equipe;

import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.servico_usuario_equipe.servico_usuario_equipe.model.converter.EquipeConverter;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.EquipeDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.EquipeModel;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Equipe.CriaEquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipe")

public class CriaEquipeController {

    @Autowired
    private EquipeConverter equipeConverter;
    @Autowired
    private CriaEquipeService criaEquipeService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarEquipe(@Valid @RequestBody EquipeDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        if (dto.getEquNome() == null || dto.getEquNome().isBlank()) {
            throw new EquipeSemInformacaoException("Erro ao cadastrar equipe", "O nome da equipe é obrigatório.");
        }
        criaEquipeService.criar(dto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body("Equipe cadastrada com sucesso!");
    }
}