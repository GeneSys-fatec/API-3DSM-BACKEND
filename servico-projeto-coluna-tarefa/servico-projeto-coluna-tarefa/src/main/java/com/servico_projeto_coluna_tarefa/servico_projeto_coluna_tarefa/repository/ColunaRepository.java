package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ColunaModel;

public interface ColunaRepository extends MongoRepository<ColunaModel, String> {

    @Query(value = "{ 'projId' : ?0 }", sort = "{ 'colOrdem' : 1 }")
    List<ColunaModel> findByProjIdOrderByColOrdemAsc(String projId);

    long countByProjId(String projId);

    long countByProjIdAndColTitulo(String projId, String colTitulo);

    long countByProjIdAndColTituloAndColIdNot(String projId, String colTitulo, String colId);

    boolean existsByProjIdAndColTitulo(String projId, String colTitulo);

    // --- NOVO MÉTODO ADICIONADO ---
    void deleteByProjId(String projId);
}