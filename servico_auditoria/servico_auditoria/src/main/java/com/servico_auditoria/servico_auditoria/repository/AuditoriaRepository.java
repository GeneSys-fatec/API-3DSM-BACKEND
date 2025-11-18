package main.java.com.servico_auditoria.servico_auditoria.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.servico_auditoria.servico_auditoria.model.AuditoriaEvento;

public interface AuditoriaRepository extends MongoRepository<AuditoriaEvento, String> {
    List<AuditoriaEvento> findByProjetoIdOrderByDataDesc(String projetoId);
}
