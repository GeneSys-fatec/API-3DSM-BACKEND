package com.servico_auditoria.servico_auditoria.service;

import com.servico_auditoria.servico_auditoria.model.AuditoriaLog;
import com.servico_auditoria.servico_auditoria.model.dto.AuditoriaResponseDto;
import com.servico_auditoria.servico_auditoria.model.dto.ModificacaoLogDto;
import com.servico_auditoria.servico_auditoria.model.dto.ResponsavelAlteracaoDto;
import com.servico_auditoria.servico_auditoria.repository.AuditoriaLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class AuditoriaService {

    private final AuditoriaLogRepository auditoriaLogRepository;

    public AuditoriaService(AuditoriaLogRepository auditoriaLogRepository) {
        this.auditoriaLogRepository = auditoriaLogRepository;
    }

    /**
     * Registra um log de auditoria unificado.
     */
    public AuditoriaLog registrarLog(String projetoId,
                                     String tarefaId,
                                     String tarefaNome,
                                     List<ModificacaoLogDto> modificacoes,
                                     String responsavelId,
                                     String responsavelEmail,
                                     String responsavelNome,
                                     String traceId) {

        AuditoriaLog log = new AuditoriaLog();

        log.setProjetoId(projetoId);
        log.setTarefaId(tarefaId);
        log.setTarefaNome(tarefaNome);

        log.setCriadoEm(LocalDateTime.now());
        log.setTraceId(traceId != null && !traceId.isBlank() ? traceId : UUID.randomUUID().toString());

        ResponsavelAlteracaoDto responsavel = new ResponsavelAlteracaoDto();
        responsavel.setUsuId(responsavelId);
        responsavel.setUsuEmail(responsavelEmail);
        responsavel.setUsuNome(responsavelNome != null ? responsavelNome : "Usuário " + responsavelId);

        log.setResponsavel(responsavel);
        log.setModificacoes(modificacoes);

        return auditoriaLogRepository.save(log);
    }

    public List<AuditoriaResponseDto> listarPorTarefaId(String tarefaId) {
        List<AuditoriaLog> logs = auditoriaLogRepository.findAllByTarefaId(tarefaId);
        return mapearResponses(logs);
    }

    public List<AuditoriaResponseDto> listarPorProjetoId(String projetoId) {
        List<AuditoriaLog> logs = auditoriaLogRepository.findAllByProjetoId(projetoId);
        return mapearResponses(logs);
    }

    public List<AuditoriaResponseDto> listarTodos() {
        return mapearResponses(auditoriaLogRepository.findAll());
    }

    private List<AuditoriaResponseDto> mapearResponses(List<AuditoriaLog> logs) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        return logs.stream()
                .flatMap(log -> log.getModificacoes().stream()
                        .map(modificacao -> new AuditoriaResponseDto(
                                log.getProjetoId(),
                                log.getTarefaId(),
                                log.getTarefaNome(),
                                log.getResponsavel(),
                                modificacao,
                                log.getCriadoEm().format(formatoData),
                                log.getCriadoEm().format(formatoHora),
                                log.getTraceId()
                        ))
                ).toList();
    }

    public AuditoriaLog registrarComContexto(String projetoId,
                                             String tarefaId,
                                             String tarefaNome,
                                             String responsavelId,
                                             String responsavelEmail,
                                             List<ModificacaoLogDto> modificacoes,
                                             String headerUserId,
                                             String headerUserName,
                                             String traceId) {

        String finalId = (headerUserId != null && !headerUserId.isBlank()) ? headerUserId : responsavelId;
        String finalNome = (headerUserName != null && !headerUserName.isBlank()) ? headerUserName : responsavelEmail;
        String finalEmail = responsavelEmail;

        return registrarLog(projetoId, tarefaId, tarefaNome, modificacoes, finalId, finalEmail, finalNome, traceId);
    }
}