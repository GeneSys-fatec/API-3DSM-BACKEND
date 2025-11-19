package com.servico_auditoria.servico_auditoria.service;

import com.servico_auditoria.servico_auditoria.model.AuditoriaEvento;
import com.servico_auditoria.servico_auditoria.model.AuditoriaLog;
import com.servico_auditoria.servico_auditoria.model.dto.AuditoriaResponseDto;
import com.servico_auditoria.servico_auditoria.model.dto.CategoriaModificacao;
import com.servico_auditoria.servico_auditoria.model.dto.ModificacaoLogDto;
import com.servico_auditoria.servico_auditoria.model.dto.ResponsavelAlteracaoDto;
import com.servico_auditoria.servico_auditoria.repository.AuditoriaLogRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditoriaService {

    private final AuditoriaLogRepository auditoriaLogRepository;
    private final MongoTemplate mongoTemplate;

    public AuditoriaService(AuditoriaLogRepository auditoriaLogRepository,
                            MongoTemplate mongoTemplate) {
        this.auditoriaLogRepository = auditoriaLogRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public AuditoriaLog registrarCriacao(String projetoId, String tarefaId, String titulo, String responsavelId, String responsavelEmail, String traceId) {
        List<ModificacaoLogDto> modificacoes = new ArrayList<>();
        modificacoes.add(new ModificacaoLogDto(CategoriaModificacao.CRIACAO, "Tarefa criada com título '" + titulo + "'."));

        String usuario = firstNonBlank(responsavelEmail, responsavelId);
        String acao = "CRIACAO: Tarefa criada com título '" + titulo + "'.";
        salvarEvento(projetoId, tarefaId, usuario, acao, traceId);

        return salvarLog(projetoId, tarefaId, modificacoes, responsavelId, responsavelEmail, traceId);
    }

    public AuditoriaLog registrarExclusao(String projetoId, String tarefaId, String titulo, String responsavelId, String responsavelEmail, String traceId) {
        List<ModificacaoLogDto> modificacoes = new ArrayList<>();
        modificacoes.add(new ModificacaoLogDto(CategoriaModificacao.EXCLUSAO, "Tarefa removida: '" + titulo + "'."));

        String usuario = firstNonBlank(responsavelEmail, responsavelId);
        String acao = "EXCLUSAO: Tarefa removida: '" + titulo + "'.";
        salvarEvento(projetoId, tarefaId, usuario, acao, traceId);

        return salvarLog(projetoId, tarefaId, modificacoes, responsavelId, responsavelEmail, traceId);
    }

    public AuditoriaLog registrarAtualizacao(String projetoId, String tarefaId, List<ModificacaoLogDto> modificacoes, String responsavelId, String responsavelEmail, String traceId) {
        String usuario = firstNonBlank(responsavelEmail, responsavelId);
        String detalhes = (modificacoes == null || modificacoes.isEmpty())
                ? ""
                : modificacoes.stream().map(Object::toString).collect(Collectors.joining(" | "));
        String acao = detalhes.isBlank() ? "ATUALIZACAO" : "ATUALIZACAO: " + detalhes;
        salvarEvento(projetoId, tarefaId, usuario, acao, traceId);

        return salvarLog(projetoId, tarefaId, modificacoes, responsavelId, responsavelEmail, traceId);
    }

    public AuditoriaLog registrarAtribuicao(String projetoId, String tarefaId, String usuarioAnterior, String usuarioNovo, String responsavelId, String responsavelEmail, String traceId) {
        List<ModificacaoLogDto> modificacoes = new ArrayList<>();
        CategoriaModificacao categoria;
        try {
            categoria = CategoriaModificacao.valueOf("ATRIBUICAO");
        } catch (IllegalArgumentException e) {
            categoria = null;
        }
        modificacoes.add(new ModificacaoLogDto(categoria, "Atribuição alterada de '" + usuarioAnterior + "' para '" + usuarioNovo + "'."));

        // salva também na coleção auditoria_eventos
        String usuario = firstNonBlank(responsavelEmail, responsavelId);
        String acao = "ATRIBUICAO: de '" + usuarioAnterior + "' para '" + usuarioNovo + "'.";
        salvarEvento(projetoId, tarefaId, usuario, acao, traceId);

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

    private void salvarEvento(String projetoId, String tarefaId, String usuario, String acao, String traceId) {
        AuditoriaEvento evento = AuditoriaEvento.builder()
                .projetoId(projetoId)
                .tarefaId(tarefaId)
                .usuario(usuario)
                .acao(acao)
                .traceId(traceId)
                .data(LocalDateTime.now())
                .build();
        mongoTemplate.save(evento);
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

    public AuditoriaLog registrarComContexto(String projetoId,
                                             String tarefaId,
                                             String bodyResponsavelId,
                                             String bodyResponsavelEmail,
                                             List<ModificacaoLogDto> modificacoes,
                                             String headerUserId,
                                             String headerUser,
                                             String traceId) {

        String usuarioId = null;
        String usuarioEmail = null;

        UsuarioInfo info = tentarExtrairUsuarioDoSecurityContext();
        if (info != null) {
            usuarioId = info.id();
            usuarioEmail = info.email();
        }

        if (isBlank(usuarioId) && headerUserId != null && !headerUserId.isBlank()) {
            usuarioId = headerUserId;
        }
        if (isBlank(usuarioEmail) && headerUser != null && !headerUser.isBlank()) {
            usuarioEmail = headerUser;
        }

        if (isBlank(usuarioId)) {
            usuarioId = bodyResponsavelId;
        }
        if (isBlank(usuarioEmail)) {
            usuarioEmail = bodyResponsavelEmail;
        }

        return registrarAtualizacao(projetoId, tarefaId, modificacoes, usuarioId, usuarioEmail, traceId);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    // Não depende de spring-security em compilação; usa reflexão se estiver no classpath.
    private UsuarioInfo tentarExtrairUsuarioDoSecurityContext() {
        try {
            Class<?> schClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object context = schClass.getMethod("getContext").invoke(null);
            if (context == null) return null;

            Object auth = context.getClass().getMethod("getAuthentication").invoke(context);
            if (auth == null) return null;

            Object principal = auth.getClass().getMethod("getPrincipal").invoke(auth);
            if (principal == null) return null;

            String id = invokeIfExists(principal, "id");
            String email = firstNonBlank(
                invokeIfExists(principal, "email"),
                invokeIfExists(principal, "getEmail"),
                invokeIfExists(principal, "nome"),
                invokeIfExists(principal, "getUsername")
            );

            if (isBlank(id) && isBlank(email)) return null;
            return new UsuarioInfo(id, email);
        } catch (Throwable t) {
            return null;
        }
    }

    private String invokeIfExists(Object target, String methodName) {
        try {
            Method m = target.getClass().getMethod(methodName);
            Object v = m.invoke(target);
            return v != null ? v.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String firstNonBlank(String... vals) {
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private record UsuarioInfo(String id, String email) {}
}
