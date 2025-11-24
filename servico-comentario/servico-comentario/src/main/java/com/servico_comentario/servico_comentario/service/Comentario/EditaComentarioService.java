package com.servico_comentario.servico_comentario.service.Comentario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;
import com.servico_comentario.servico_comentario.repository.ComentarioRepository;

@Service
public class EditaComentarioService {

    @Autowired
    private ComentarioRepository repository;

    @Autowired
    private ValidaComentarioService validaComentarioService;

    public ComentarioModel atualizarComentario(ComentarioModel comentario) {

        validaComentarioService.validarComentario(comentario);

        validaComentarioService.verificarSeUsuarioPodeAlterarComentario(comentario);

        return repository.save(comentario);
    }
}
