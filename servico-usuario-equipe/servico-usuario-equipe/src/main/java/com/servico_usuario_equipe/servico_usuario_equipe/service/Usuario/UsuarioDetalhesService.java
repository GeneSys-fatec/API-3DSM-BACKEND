package com.servico_usuario_equipe.servico_usuario_equipe.service.Usuario;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.servico_usuario_equipe.servico_usuario_equipe.repository.UsuarioRepository;

public class UsuarioDetalhesService implements UserDetailsService {
    private UsuarioRepository usuarioRepository;

    public UsuarioDetalhesService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + id));
    }

}
