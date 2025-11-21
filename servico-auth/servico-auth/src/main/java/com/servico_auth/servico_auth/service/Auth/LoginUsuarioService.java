package com.servico_auth.servico_auth.service.Auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.servico_auth.servico_auth.exceptions.personalizados.autenticação.CredenciaisInvalidasException;
import com.servico_auth.servico_auth.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.servico_auth.servico_auth.model.dto.resposta.ResponseDTO;
import com.servico_auth.servico_auth.model.dto.usuario.UsuarioLoginDTO;
import com.servico_auth.servico_auth.model.entidade.UsuarioModel;
import com.servico_auth.servico_auth.repository.UsuarioRepository;
import com.servico_auth.servico_auth.service.Token.CriaTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CriaTokenService criaTokenService;

    private UsuarioModel validarCredenciais(UsuarioLoginDTO body) {
        UsuarioModel usuario = this.usuarioRepository.findByEmail(body.getUsuEmail().toLowerCase())
        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado.", "Email não encontrado."));

        if (!passwordEncoder.matches(body.getUsuSenha(), usuario.getPassword())) {
            throw new CredenciaisInvalidasException("Credenciais inválidas.", "Senha incorreta.");
        }
        
        return usuario;
    }

    public ResponseDTO loginUsuario(UsuarioLoginDTO body) {
        UsuarioModel usuario = this.validarCredenciais(body);
        
        return new ResponseDTO(
            usuario.getUsuId(), 
            usuario.getUsuNome(), 
            usuario.getUsuEmail(), 
            usuario.getUsuCaminhoFoto()
        );
    }

    public String generateTokenForUser(UsuarioLoginDTO body) {
        UsuarioModel usuario = this.validarCredenciais(body);
        return this.criaTokenService.generateToken(usuario);
    }
}
