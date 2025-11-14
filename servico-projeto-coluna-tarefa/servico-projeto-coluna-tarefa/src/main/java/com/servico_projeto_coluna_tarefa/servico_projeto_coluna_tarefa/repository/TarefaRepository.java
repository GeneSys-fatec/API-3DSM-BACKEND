package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;

public interface TarefaRepository extends MongoRepository<TarefaModel, String> {

    @Query("{ 'projId': { '$in': ?0 } }")
    List<TarefaModel> findByProjIdIn(List<String> projetoIds);

    @Query("{ 'projId': ?0 }")
    List<TarefaModel> findByProjId(String projId);

    List<TarefaModel> findByTarPrazo(LocalDate tarPrazo);

    List<TarefaModel> findByTarPrazoBeforeAndTarStatusNot(LocalDate tarPrazo, String status);

    List<TarefaModel> findByResponsaveisUsuId(String usuId);

    // --- NOVO MÉTODO ADICIONADO ---
    // (O Spring Data MongoDB cria a query automaticamente pelo nome do método)
    void deleteByProjId(String projId);
}