package com.servico_auditoria.servico_auditoria.repository;

import com.servico_auditoria.servico_auditoria.model.AuditoriaLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaLogRepository extends MongoRepository<AuditoriaLog, String> {
    List<AuditoriaLog> findAllByTarefaId(String tarefaId);
    List<AuditoriaLog> findAllByProjetoId(String projetoId);
}
