package com.servico_usuario_equipe.servico_usuario_equipe.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.ResetaSenhaModel;

public interface ResetaSenhaRepository extends MongoRepository<ResetaSenhaModel, String> {
    Optional<ResetaSenhaModel> findByToken(String token);
}
