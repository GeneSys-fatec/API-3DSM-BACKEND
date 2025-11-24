package com.servico_comentario.servico_comentario.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;

public interface ComentarioRepository extends MongoRepository<ComentarioModel, String> {
    List<ComentarioModel> findBytarId(String tarId);
    @Query("{ 'comResposta': ?0 }")
    List<ComentarioModel> findByRespostaComentario(String comResposta);
}
