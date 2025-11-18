package main.java.com.servico_auditoria.servico_auditoria.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servico_auditoria.servico_auditoria.model.AuditoriaEvento;
import com.servico_auditoria.servico_auditoria.repository.AuditoriaRepository;

@Service
public class AuditoriaService {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaService.class);

    private final AuditoriaRepository repository;

    public AuditoriaService(AuditoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AuditoriaEvento registrarEvento(String projetoId, String tarefaId, String usuario, String acao, String traceId) {
        AuditoriaEvento evento = AuditoriaEvento.builder()
                .projetoId(projetoId)
                .tarefaId(tarefaId)
                .usuario(usuario)
                .acao(acao)
                .traceId(traceId)
                .data(LocalDateTime.now())
                .build();

        AuditoriaEvento salvo = repository.save(evento);
        logger.info("Auditoria registrada: projetoId={}, tarefaId={}, usuario={}, acao={}, traceId={}", projetoId, tarefaId, usuario, acao, traceId);
        return salvo;
    }
}
