package com.servico_comentario.servico_comentario.service.Comentario;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;
import com.servico_comentario.servico_comentario.repository.ComentarioRepository;

@Service
public class BuscaComentarioService {
    @Autowired
    private ComentarioRepository repository;

    public List<ComentarioModel> listarPorTarefa(String tarId) {
        if (tarId == null || tarId.isBlank()) {
            return Collections.emptyList();
        }
        return repository.findBytarId(tarId);    }

    public List<ComentarioModel> listarTodos() {
        return repository.findAll();
    }

    public Optional<ComentarioModel> listarPorId(String comId) {
        return repository.findById(comId);
    }
}