package com.servico_usuario_equipe.servico_usuario_equipe.service.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.UsuarioRepository;

@Service
public class EditaUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioModel salvar(UsuarioModel usuario) {
        usuario.setUsuSenha(encoder.encode(usuario.getUsuSenha()));
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizar(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }
}
