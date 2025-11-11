
package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.coluna.LimiteDeColunasExcedidoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.coluna.NomeDeColunaJaExisteException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoSemInformacaoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.tarefas.AnexoTamanhoExcedente;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ErroRespostaDTO;

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


    @ExceptionHandler(ProjetoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularProjetoNaoEncontrado(ProjetoNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ProjetoSemInformacaoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularProjetoSemInformacao(ProjetoSemInformacaoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(InvalidTaskDataException.class)
    public ResponseEntity<ErroRespostaDTO> manipularInvalidTaskData(InvalidTaskDataException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }


    @ExceptionHandler(AcessoNaoAutorizadoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularAcessoNaoAutorizado(AcessoNaoAutorizadoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(AnexoTamanhoExcedente.class)
    public ResponseEntity<ErroRespostaDTO> manipularAnexoTamanhoExcedente(AnexoTamanhoExcedente ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(erro);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErroRespostaDTO> manipularUploadExcedido(MaxUploadSizeExceededException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), "O tamanho do arquivo excede o limite permitido.");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(erro);
    }


    @ExceptionHandler(NomeDeColunaJaExisteException.class)
    public ResponseEntity<ErroRespostaDTO> manipularNomeDeColunaJaExiste(NomeDeColunaJaExisteException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(LimiteDeColunasExcedidoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularLimiteDeColunas(LimiteDeColunasExcedidoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

}