package com.servico_usuario_equipe.servico_usuario_equipe.controller.Equipe;

import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Equipe.ExcluiEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipe")

public class ExcluiEquipeController {
    @Autowired
    private ExcluiEquipeService excluiEquipeService;

    @DeleteMapping("/apagar/{equId}")
    public ResponseEntity<String> apagarEquipe(@PathVariable String equId, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        excluiEquipeService.excluir(equId, usuarioLogado);
        return ResponseEntity.ok("Equipe apagada com sucesso!");
    }
}