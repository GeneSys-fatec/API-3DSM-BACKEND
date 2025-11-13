package com.servico_usuario_equipe.servico_usuario_equipe.controller.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_usuario_equipe.servico_usuario_equipe.model.converter.UsuarioConverter;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.usuario.UsuarioDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SessaoUsuarioController {
    
    @Autowired
    private UsuarioConverter usuarioconverter;

    @GetMapping("/session")
    public ResponseEntity<UsuarioDTO> checkSession(
            @AuthenticationPrincipal UsuarioModel usuarioModel
    ) {
        
        if (usuarioModel == null) {
             return ResponseEntity.status(401).build();
        }

        UsuarioDTO usuarioDTO = usuarioconverter.modelParaDto(usuarioModel);
        
        return ResponseEntity.ok(usuarioDTO);
    }
}