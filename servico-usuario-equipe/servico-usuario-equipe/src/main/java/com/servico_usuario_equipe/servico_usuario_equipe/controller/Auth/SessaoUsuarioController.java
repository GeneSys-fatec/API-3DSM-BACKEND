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
            // 1. Receba o UsuarioModel que o filtro colocou no contexto
            @AuthenticationPrincipal UsuarioModel usuarioModel
    ) {
        
        if (usuarioModel == null) {
            // Isso não deve acontecer se o filtro funcionar, mas é uma boa defesa
             return ResponseEntity.status(401).build();
        }

        // 2. Converta o Model para DTO usando o Converter
        UsuarioDTO usuarioDTO = usuarioconverter.modelParaDto(usuarioModel);
        
        // 3. Retorne o DTO
        return ResponseEntity.ok(usuarioDTO);
    }
}