package com.servico_usuario_equipe.servico_usuario_equipe.service.Auth;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação.SenhasNaoCoincidemException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.usuário.EmailJaCadastradoException;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.ResponseDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.usuario.UsuarioCadastroDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastroUsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseDTO cadastrarUsuario(UsuarioCadastroDTO body) {
        Optional<UsuarioModel> usuarioOpt = this.usuarioRepository.findByEmail(body.getUsuEmail().toLowerCase());

        if (usuarioOpt.isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado.", "Este email já está sendo usado em outra conta.");
        }

        if (!body.getUsuSenha().equals(body.getUsuConfirmarSenha())) {
            throw new SenhasNaoCoincidemException("Confirmação de senha", "As senhas não coincidem.");
        }

        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setUsuSenha(passwordEncoder.encode(body.getUsuSenha()));
        novoUsuario.setUsuEmail(body.getUsuEmail().toLowerCase());
        novoUsuario.setUsuNome(body.getUsuNome());
        
        UsuarioModel usuarioSalvo = this.usuarioRepository.save(novoUsuario);
        
        return new ResponseDTO(
            usuarioSalvo.getUsuId(), 
            usuarioSalvo.getUsuNome(),
            usuarioSalvo.getUsuEmail(),
            usuarioSalvo.getUsuCaminhoFoto() 
        );
    }
}