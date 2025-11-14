package com.servico_comentario.servico_comentario.service.Comentario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;
import com.servico_comentario.servico_comentario.repository.ComentarioRepository;

@Service
public class ExcluiComentarioService {

    @Autowired
    private ComentarioRepository repository;

    public void deletarRespostaComentario(String comId) {
        List<ComentarioModel> respostas = repository.findByRespostaComentario(comId);
        if (respostas != null && !respostas.isEmpty()) {
            for (ComentarioModel resposta : respostas) {
                deletarRespostaComentario(resposta.getComId());
            }
        }
        repository.deleteById(comId);
    }
}