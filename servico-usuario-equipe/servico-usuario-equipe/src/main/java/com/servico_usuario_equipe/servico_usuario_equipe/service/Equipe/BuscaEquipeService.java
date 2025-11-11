package com.servico_usuario_equipe.servico_usuario_equipe.service.Equipe;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.EquipeNaoEncontradaException;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.EquipeModel;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.EquipeRepository;

@Service
public class BuscaEquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    public List<EquipeModel> getAllEquipes() {
        return equipeRepository.findAll();
    }

    public EquipeModel getEquipeById(String id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new EquipeNaoEncontradaException(
                        "Equipe Não Encontrada",
                        "A equipe com o ID " + id + " não foi encontrada no sistema."
                ));
    }

    public List<EquipeModel> getEquipesPorIdUsuario(String usuarioId) {
        return equipeRepository.findByUsuariosUsuId(usuarioId);
    }

    public List<EquipeModel> getEquipesPorUsuario(UsuarioModel usuario) {
        if (usuario == null) {
            return Collections.emptyList();
        }
        return equipeRepository.findByUsuariosUsuId(usuario.getUsuId());
    }

    public boolean isUsuarioMembro(String equipeId, String usuarioId) {
        
        EquipeModel equipe = this.getEquipeById(equipeId);
        
        return equipe.getUsuarios().stream()
            .anyMatch(usuario -> usuario.getUsuId().equals(usuarioId));
    }
}
