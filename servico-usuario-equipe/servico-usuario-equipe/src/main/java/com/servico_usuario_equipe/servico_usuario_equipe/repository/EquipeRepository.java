package com.servico_usuario_equipe.servico_usuario_equipe.repository;

import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.EquipeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EquipeRepository extends MongoRepository<EquipeModel, String> {
    Optional<EquipeModel> findByEquNome(String equNome);
    @Query("{ 'usuarios.usuId' : ?0 }")
    List<EquipeModel> findByUsuariosUsuId(String usuId);
}