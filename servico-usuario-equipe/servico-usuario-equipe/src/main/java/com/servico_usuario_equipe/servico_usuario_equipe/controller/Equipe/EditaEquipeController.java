package com.servico_usuario_equipe.servico_usuario_equipe.controller.Equipe;

import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.EquipeDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Equipe.EditaEquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipe")

public class EditaEquipeController {

    @Autowired
    private EditaEquipeService editaEquipeService;

    @PutMapping("/atualizar/{equId}")
    // CORRIGIDO: Adicionado @AuthenticationPrincipal
    public ResponseEntity<String> atualizarEquipe(@PathVariable String equId, @Valid @RequestBody EquipeDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        if (dto.getEquNome() == null || dto.getEquNome().isBlank()) {
            throw new EquipeSemInformacaoException("Erro ao atualizar equipe", "O nome da equipe não pode ser nulo ou vazio.");
        }
        editaEquipeService.editar(equId, dto, usuarioLogado);
        return ResponseEntity.ok("Equipe atualizada com sucesso!");
    }
}