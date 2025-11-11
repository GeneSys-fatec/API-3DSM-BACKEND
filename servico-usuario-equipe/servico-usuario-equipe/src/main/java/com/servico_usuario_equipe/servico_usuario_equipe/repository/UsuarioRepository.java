package com.servico_usuario_equipe.servico_usuario_equipe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;

public interface UsuarioRepository extends MongoRepository<UsuarioModel, String> {
    @Query("{ 'usuEmail' : ?0 }")
    Optional<UsuarioModel> findByEmail(String email);

    @Query("{ 'usuEmail' : { $in: ?0 } }")
    List<UsuarioModel> findAllByEmails(List<String> emails);

    List<UsuarioModel> findByUsuIdIn(List<String> ids);
}