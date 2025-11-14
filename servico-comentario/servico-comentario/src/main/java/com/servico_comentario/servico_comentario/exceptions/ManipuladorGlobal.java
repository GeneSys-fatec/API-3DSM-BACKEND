package com.servico_comentario.servico_comentario.exceptions;

import java.util.stream.Collectors;

import com.servico_comentario.servico_comentario.exceptions.personalizados.comentário.ComentarioEmBrancoException;
import com.servico_comentario.servico_comentario.exceptions.personalizados.comentário.ConteudoInapropriadoException;
import com.servico_comentario.servico_comentario.model.dto.ErroRespostaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException; // Import para segurança



@ControllerAdvice
public class ManipuladorGlobal {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> manipularValidacaoCampos(MethodArgumentNotValidException ex) {
        String mensagens = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErroRespostaDTO erro = new ErroRespostaDTO("Erro de validação", mensagens);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(ComentarioEmBrancoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularComentarioEmBranco(ComentarioEmBrancoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ConteudoInapropriadoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularComentarioInapropriado(ConteudoInapropriadoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroRespostaDTO> manipularAcessoNegado(AccessDeniedException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO("Acesso Negado", "Você não tem permissão para executar esta ação.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> manipularErroGenerico(Exception ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO("Erro Interno do Servidor", "Ocorreu um erro inesperado.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}