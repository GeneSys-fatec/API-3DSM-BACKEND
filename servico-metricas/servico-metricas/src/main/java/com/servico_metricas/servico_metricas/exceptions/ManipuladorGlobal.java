package com.servico_metricas.servico_metricas.exceptions;

import com.servico_metricas.servico_metricas.model.dto.ErroRespostaDTO;
import com.servico_metricas.servico_metricas.exceptions.personalizados.métricas.DashboardSemDadosException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// Importe as duas exceções do WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class ManipuladorGlobal {

    @ExceptionHandler(DashboardSemDadosException.class)
    public ResponseEntity<ErroRespostaDTO> manipularDashboardSemDados(DashboardSemDadosException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    /**
     * Handler #2: Trata erros de RESPOSTA (ex: 404, 503)
     * Disparado se o MS-Tarefas responder com um erro.
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErroRespostaDTO> manipularErroWebClientResponse(WebClientResponseException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                "Erro de Comunicação",
                "O serviço de tarefas respondeu com um erro: " + ex.getStatusCode()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(erro);
    }

    /**
     * Handler #3: NOVO e ESSENCIAL
     * Trata erros de REQUISIÇÃO (ex: Connection Refused)
     * Disparado se o MS-Métricas não conseguir NEM SEQUER se conectar ao Gateway.
     */
    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ErroRespostaDTO> manipularErroWebClientRequest(WebClientRequestException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                "Erro de Conexão",
                "Não foi possível conectar ao serviço de tarefas (Gateway). Causa: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(erro);
    }

    /**
     * Handler #4: "Pega-tudo" (Catch-all)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> manipularExcecaoGenerica(Exception ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                "Erro Interno Inesperado",
                "Causa: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}