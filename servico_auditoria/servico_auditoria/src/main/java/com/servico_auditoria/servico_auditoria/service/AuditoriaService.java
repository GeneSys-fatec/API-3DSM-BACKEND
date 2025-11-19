package com.servico_auditoria.servico_auditoria.service;

import com.servico_auditoria.servico_auditoria.model.AuditoriaLog;
import com.servico_auditoria.servico_auditoria.model.dto.AuditoriaResponseDto;
import com.servico_auditoria.servico_auditoria.model.dto.CategoriaModificacao;
import com.servico_auditoria.servico_auditoria.model.dto.ModificacaoLogDto;
import com.servico_auditoria.servico_auditoria.model.dto.ResponsavelAlteracaoDto;
import com.servico_auditoria.servico_auditoria.repository.AuditoriaLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaLogRepository auditoriaLogRepository;

    public AuditoriaService(AuditoriaLogRepository auditoriaLogRepository) {
        this.auditoriaLogRepository = auditoriaLogRepository;
    }

    public AuditoriaLog registrarCriacao(String projetoId, String tarefaId, String titulo, String responsavelId, String responsavelEmail, String traceId) {
        List<ModificacaoLogDto> modificacoes = new ArrayList<>();
        modificacoes.add(new ModificacaoLogDto(CategoriaModificacao.CRIACAO, "Tarefa criada com título '" + titulo + "'."));
        return salvarLog(projetoId, tarefaId, modificacoes, responsavelId, responsavelEmail, traceId);
    }

    public AuditoriaLog registrarExclusao(String projetoId, String tarefaId, String titulo, String responsavelId, String responsavelEmail, String traceId) {
        List<ModificacaoLogDto> modificacoes = new ArrayList<>();
        modificacoes.add(new ModificacaoLogDto(CategoriaModificacao.EXCLUSAO, "Tarefa removida: '" + titulo + "'."));
        return salvarLog(projetoId, tarefaId, modificacoes, responsavelId, responsavelEmail, traceId);
    }

    public AuditoriaLog registrarAtualizacao(String projetoId, String tarefaId, List<ModificacaoLogDto> modificacoes, String responsavelId, String responsavelEmail, String traceId) {
        return salvarLog(projetoId, tarefaId, modificacoes, responsavelId, responsavelEmail, traceId);
    }

    public AuditoriaLog registrarAtribuicao(String projetoId, String tarefaId, String usuarioAnterior, String usuarioNovo, String responsavelId, String responsavelEmail, String traceId) {
        List<ModificacaoLogDto> modificacoes = new ArrayList<>();
        modificacoes.add(new ModificacaoLogDto(CategoriaModificacao.EDICAO, "Responsável alterado de '" + usuarioAnterior + "' para '" + usuarioNovo + "'."));
        return salvarLog(projetoId, tarefaId, modificacoes, responsavelId, responsavelEmail, traceId);
    }

    private AuditoriaLog salvarLog(String projetoId, String tarefaId, List<ModificacaoLogDto> modificacoes, String responsavelId, String responsavelEmail, String traceId) {
        AuditoriaLog log = new AuditoriaLog();
        log.setProjetoId(projetoId);
        log.setTarefaId(tarefaId);
        log.setCriadoEm(LocalDateTime.now());
        log.setResponsavel(new ResponsavelAlteracaoDto(responsavelId, responsavelEmail));
        log.setModificacoes(modificacoes);
        log.setTraceId(traceId);
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
        List<AuditoriaLog> logs = auditoriaLogRepository.findAll();
        return mapearResponses(logs);
    }

    private List<AuditoriaResponseDto> mapearResponses(List<AuditoriaLog> logs) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        return logs.stream()
                .flatMap(log -> log.getModificacoes().stream()
                        .map(modificacao -> new AuditoriaResponseDto(
                                log.getProjetoId(),
                                log.getTarefaId(),
                                log.getResponsavel(),
                                modificacao,
                                log.getCriadoEm().format(formatoData),
                                log.getCriadoEm().format(formatoHora),
                                log.getTraceId()
                        ))
                ).toList();
    }
}
