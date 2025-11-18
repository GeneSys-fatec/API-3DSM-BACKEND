package main.java.com.servico_auditoria.servico_auditoria.controller;

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
import com.servico_auditoria.servico_auditoria.repository.AuditoriaRepository;
import com.servico_auditoria.servico_auditoria.service.AuditoriaService;
import com.servico_auditoria.servico_auditoria.model.dto.AuditoriaEventoRequest;

@RestController
@RequestMapping(path = "/auditoria", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditoriaController {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaController.class);

    private final AuditoriaRepository repository;
    private final AuditoriaService service;

    public AuditoriaController(AuditoriaRepository repository, AuditoriaService service) {
        this.repository = repository;
        this.service = service;
    }

    // Lista eventos por projeto (ordem desc)
    @GetMapping("/projeto/{id}")
    public ResponseEntity<List<AuditoriaEvento>> listarPorProjeto(
            @PathVariable("id") String projetoId,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        if (traceId != null) {
            logger.info("Listando auditoria do projeto={} traceId={}", projetoId, traceId);
        }
        List<AuditoriaEvento> eventos = repository.findByProjetoIdOrderByDataDesc(projetoId);
        return ResponseEntity.ok(eventos);
    }

    // Registrar via POST opcional
    @PostMapping(path = "/evento", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditoriaEvento> registrar(
            @Validated @RequestBody AuditoriaEventoRequest req,
            @RequestHeader(value = "X-Trace-Id", required = false) String headerTraceId,
            @RequestHeader(value = "X-User", required = false) String headerUser) {

        String traceId = headerTraceId != null && !headerTraceId.isBlank() ? headerTraceId : req.getTraceId();
        String usuario = headerUser != null && !headerUser.isBlank() ? headerUser : req.getUsuario();

        AuditoriaEvento salvo = service.registrarEvento(
                req.getProjetoId(),
                req.getTarefaId(),
                usuario,
                req.getAcao(),
                traceId
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(salvo);
    }
}
