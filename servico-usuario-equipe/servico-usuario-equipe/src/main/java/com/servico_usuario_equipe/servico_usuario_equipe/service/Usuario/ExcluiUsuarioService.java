package com.servico_usuario_equipe.servico_usuario_equipe.service.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_usuario_equipe.servico_usuario_equipe.repository.UsuarioRepository;

@Service
public class ExcluiUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void deletar(String id) {
        usuarioRepository.deleteById(id);
    }
}
