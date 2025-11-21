package com.servico_usuario_equipe.servico_usuario_equipe.controller.Auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.ResponseDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.usuario.UsuarioCadastroDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Auth.CadastroUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class CadastroUsuarioController {

    private final CadastroUsuarioService cadastroUsuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrar(@RequestBody @Valid UsuarioCadastroDTO body) {
        ResponseDTO novoUsuarioResponse = cadastroUsuarioService.cadastrarUsuario(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuarioResponse);
    }
}
