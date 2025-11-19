package com.servico_auditoria.servico_auditoria.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.servico_auditoria.servico_auditoria.model.AuditoriaLog;
import com.servico_auditoria.servico_auditoria.model.dto.AuditoriaResponseDto;
import com.servico_auditoria.servico_auditoria.model.dto.ModificacaoLogDto;
import com.servico_auditoria.servico_auditoria.service.AuditoriaService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping(path = "/auditoria/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditoriaControle {

    private final AuditoriaService auditoriaLogService;

    public AuditoriaControle(AuditoriaService auditoriaLogService) {
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditoriaLog> registrar(@RequestBody @Valid RegistrarLogRequest body,
                                                  @RequestHeader(value = "X-User", required = false) String headerUser,
                                                  @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
                                                  @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {

        AuditoriaLog salvo = auditoriaLogService.registrarComContexto(
                body.projetoId(),
                body.tarefaId(),
                body.responsavelId(),
                body.responsavelEmail(),
                body.modificacoes(),
                headerUserId,
                headerUser,
                traceId
        );
        return ResponseEntity.ok(salvo);
    }

    public record RegistrarLogRequest(
            @NotBlank String projetoId,
            @NotBlank String tarefaId,
            String responsavelId,
            String responsavelEmail,
            List<ModificacaoLogDto> modificacoes
    ) {}
}
