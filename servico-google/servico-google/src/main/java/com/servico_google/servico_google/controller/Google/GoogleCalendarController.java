package com.servico_google.servico_google.controller.Google;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.servico_google.servico_google.model.converter.GoogleEventConverter;
import com.servico_google.servico_google.model.dto.google.CreateEventRequestDTO;
import com.servico_google.servico_google.model.dto.google.ExchangeRequestDTO;
import com.servico_google.servico_google.model.dto.google.MessageResponseDTO;
import com.servico_google.servico_google.model.dto.google.StatusResponseDTO;
import com.servico_google.servico_google.model.dto.google.UpdateEventRequestDTO;
import com.servico_google.servico_google.service.Google.GoogleOAuthService;

@RestController
@RequestMapping("/google")
public class GoogleCalendarController {

    private final GoogleOAuthService googleOAuthService;

    public GoogleCalendarController(GoogleOAuthService googleOAuthService) {
        this.googleOAuthService = googleOAuthService;
    }

    private String currentUserKey() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new RuntimeException("unauthorized");
        return auth.getName();
    }

    @PostMapping("/exchange")
    public ResponseEntity<MessageResponseDTO> exchange(@RequestBody ExchangeRequestDTO body) {
        String code = body != null ? body.getCode() : null;
        String redirectUri = body != null ? body.getRedirectUri() : null;
        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Código de autorização ausente."));
        }
        googleOAuthService.exchangeCode(currentUserKey(), code, redirectUri);
        return ResponseEntity.ok(new MessageResponseDTO("Conta Google conectada com sucesso."));
    }

    @GetMapping("/status")
    public ResponseEntity<StatusResponseDTO> status() {
        boolean has = googleOAuthService.hasTokens(currentUserKey());
        return ResponseEntity.ok(new StatusResponseDTO(has));
    }

    @GetMapping("/events")
    public ResponseEntity<?> events(@RequestParam(required = false) String timeMin,
                                    @RequestParam(required = false) String timeMax) {
        return googleOAuthService.listEvents(currentUserKey(), timeMin, timeMax);
    }

    @PostMapping("/create-event")
    public ResponseEntity<MessageResponseDTO> createEvent(@RequestBody CreateEventRequestDTO req) {
        var eventBody = GoogleEventConverter.toCreateEventMap(req);
        var resp = googleOAuthService.createEvent(currentUserKey(), eventBody);
        HttpStatusCode st = resp.getStatusCode();
        String msg = st.is2xxSuccessful() ? "Evento criado com sucesso." : "Falha ao criar evento no Google.";
        return ResponseEntity.status(st).body(new MessageResponseDTO(msg));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<MessageResponseDTO> updateEvent(@PathVariable String eventId,
                                                          @RequestBody UpdateEventRequestDTO req) {
        var eventBody = GoogleEventConverter.toUpdateEventMap(req);
        var resp = googleOAuthService.updateEvent(currentUserKey(), eventId, eventBody);
        HttpStatusCode st = resp.getStatusCode();
        String msg = st.is2xxSuccessful() ? "Evento atualizado com sucesso." : "Falha ao atualizar evento no Google.";
        return ResponseEntity.status(st).body(new MessageResponseDTO(msg));
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<MessageResponseDTO> deleteEvent(@PathVariable String eventId) {
        var resp = googleOAuthService.deleteEvent(currentUserKey(), eventId);
        HttpStatusCode st = resp.getStatusCode();
        String msg = st.is2xxSuccessful() ? "Evento removido com sucesso." : "Falha ao remover evento no Google.";
        return ResponseEntity.status(st).body(new MessageResponseDTO(msg));
    }
}