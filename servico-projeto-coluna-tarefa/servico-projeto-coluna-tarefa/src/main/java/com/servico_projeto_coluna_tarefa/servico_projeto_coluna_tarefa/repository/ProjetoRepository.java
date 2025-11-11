package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProjetoRepository extends MongoRepository<ProjetoModel, String> {

    @Query("{ 'equipeId': { $in: ?0 } }")
    List<ProjetoModel> findByEquipeIdIn(List<String> equipeIds);

    List<ProjetoModel> findAllByProjIdIn(List<String> ids);
}