package com.servico_auditoria.servico_auditoria.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_auditoria.servico_auditoria.model.AuditoriaEvento;
import com.servico_auditoria.servico_auditoria.model.AuditoriaLog;
import com.servico_auditoria.servico_auditoria.model.dto.AuditoriaResponseDto;
import com.servico_auditoria.servico_auditoria.model.dto.ModificacaoLogDto;
import com.servico_auditoria.servico_auditoria.repository.AuditoriaLogRepository;
import com.servico_auditoria.servico_auditoria.service.AuditoriaService;
import com.servico_auditoria.servico_auditoria.service.AuditoriaService;
// import com.servico_auditoria.servico_auditoria.model.dto.AuditoriaEventoRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;


@RestController
@RequestMapping(path = "/auditoria/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditoriaController {

    private final AuditoriaService auditoriaLogService;

    public AuditoriaController(AuditoriaService auditoriaLogService) {
        this.auditoriaLogService = auditoriaLogService;
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<AuditoriaResponseDto>> listarPorTarefa(@PathVariable String tarefaId) {
        return ResponseEntity.ok(auditoriaLogService.listarPorTarefaId(tarefaId));
    }

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<AuditoriaResponseDto>> listarPorProjeto(@PathVariable String projetoId) {
        return ResponseEntity.ok(auditoriaLogService.listarPorProjetoId(projetoId));
    }

    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDto>> listarTodos() {
        return ResponseEntity.ok(auditoriaLogService.listarTodos());
    }

    // Endpoint opcional para registrar log com modificações explícitas
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditoriaLog> registrar(@RequestBody @Valid RegistrarLogRequest body,
                                                  @RequestHeader(value = "X-User", required = false) String headerUser,
                                                  @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
                                                  @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {

        String usuarioEmail = headerUser != null && !headerUser.isBlank() ? headerUser : body.responsavelEmail();
        String usuarioId = headerUserId != null && !headerUserId.isBlank() ? headerUserId : body.responsavelId();

        AuditoriaLog salvo = auditoriaLogService.registrarAtualizacao(
                body.projetoId(),
                body.tarefaId(),
                body.modificacoes(),
                usuarioId,
                usuarioEmail,
                traceId
        );
        return ResponseEntity.ok(salvo);
    }

    // DTO do POST
    public record RegistrarLogRequest(
            @NotBlank String projetoId,
            @NotBlank String tarefaId,
            String responsavelId,
            String responsavelEmail,
            List<ModificacaoLogDto> modificacoes
    ) {}
}
