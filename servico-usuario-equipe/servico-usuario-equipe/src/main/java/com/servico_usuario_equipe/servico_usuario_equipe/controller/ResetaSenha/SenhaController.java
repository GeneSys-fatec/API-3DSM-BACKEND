package com.servico_usuario_equipe.servico_usuario_equipe.controller.ResetaSenha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.resetasenha.EmailDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.resetasenha.NovaSenhaDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.service.ResetaSenha.SenhaService;

@RestController
@RequestMapping("/auth")
public class SenhaController {
    @Autowired
    private SenhaService senhaService;

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@RequestBody EmailDTO dto) {
        senhaService.esqueciMinhaSenha(dto.getEmail());
        return ResponseEntity.ok("Email de recuperação enviado!");
    }

    @PostMapping("/resetar-senha")
    public ResponseEntity<String> resetarSenha(@RequestParam("token") String token, 
                                               @RequestBody NovaSenhaDTO dto) {
        senhaService.redefinirSenha(token, dto.getSenha());
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }
}
