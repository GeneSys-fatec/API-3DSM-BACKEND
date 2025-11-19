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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditoriaLog> registrar(
            @RequestBody @Valid RegistrarLogRequest body,
            @RequestHeader(value = "X-User", required = false) String headerUser,
            @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
            @RequestHeader(value = "X-User-Email", required = false) String headerUserEmail,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {

        // Aceita projetoId OU tarefaId
        if ((body.projetoId == null || body.projetoId.isBlank())
                && (body.tarefaId == null || body.tarefaId.isBlank())) {
            return ResponseEntity.badRequest().build();
        }

        String respId = body.responsavelId != null ? body.responsavelId : headerUserId;
        String respEmail = body.responsavelEmail != null ? body.responsavelEmail : headerUserEmail;
        String respNomeHeader = headerUser; // opcional

        AuditoriaLog salvo = auditoriaLogService.registrarComContexto(
                body.projetoId,
                body.tarefaId,
                respId,
                respEmail,
                body.modificacoes,
                respId,
                respNomeHeader,
                traceId
        );
        return ResponseEntity.ok(salvo);
    }

    public static class RegistrarLogRequest {
        public String projetoId;                 // opcional
        public String tarefaId;                  // opcional
        public String responsavelId;            // opcional
        public String responsavelEmail;         // opcional
        public List<ModificacaoLogDto> modificacoes;
    }
}
