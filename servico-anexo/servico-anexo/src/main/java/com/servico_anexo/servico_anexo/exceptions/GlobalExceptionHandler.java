package com.servico_anexo.servico_anexo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.servico_anexo.servico_anexo.model.dto.ErroRespostaDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AnexoTamanhoExcedente.class)
    public ResponseEntity<ErroRespostaDTO> handleAnexoTamanhoExcedente(AnexoTamanhoExcedente ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> handleGeneric(Exception ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO("Erro interno", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}