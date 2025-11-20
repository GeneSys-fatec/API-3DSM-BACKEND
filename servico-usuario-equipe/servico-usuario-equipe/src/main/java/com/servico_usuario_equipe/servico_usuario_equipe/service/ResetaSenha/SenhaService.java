package com.servico_usuario_equipe.servico_usuario_equipe.service.ResetaSenha;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.ResetaSenhaModel;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.ResetaSenhaRepository;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.UsuarioRepository;

@Service
public class SenhaService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ResetaSenhaRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void esqueciMinhaSenha(String email) {
        UsuarioModel usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        String token = UUID.randomUUID().toString();

        ResetaSenhaModel myToken = new ResetaSenhaModel(token, usuario.getUsuEmail());
        tokenRepository.save(myToken);

        emailService.sendResetTokenEmail(usuario.getUsuEmail(), token);
    }
    public void redefinirSenha(String token, String novaSenha) {
        ResetaSenhaModel resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido ou expirado"));

        UsuarioModel usuario = usuarioRepository.findByEmail(resetToken.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setUsuSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        tokenRepository.delete(resetToken);
    }
}
