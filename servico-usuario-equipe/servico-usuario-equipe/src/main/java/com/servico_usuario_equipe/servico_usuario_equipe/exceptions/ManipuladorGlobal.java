package com.servico_usuario_equipe.servico_usuario_equipe.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação.CredenciaisInvalidasException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação.SenhaIncorretaException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação.TokenCriacaoException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação.TokenInvalidoException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.CriadorNaoPodeSairException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.EquipeNaoEncontradaException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.NomeDeEquipeJaExisteException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.UsuarioNaoEMembroException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.usuário.EmailJaCadastradoException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.ErroRespostaDTO;

@ControllerAdvice
public class ManipuladorGlobal {

    // Exception de Validação de Cadastro do UsuarioCadastroDTO
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

    // CredenciaisInvalidasException
    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroRespostaDTO> manipularCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // TokenCriacaoException
    @ExceptionHandler(TokenCriacaoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularTokenCriacao(TokenCriacaoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // TokenInvalidoException
    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularTokenInvalido(TokenInvalidoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // UsuárioNãoEncontradoException
    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // EmailJáCadastradoException
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularEmailJaCadastrado(EmailJaCadastradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // SenhaIncorretaException
    @ExceptionHandler(SenhaIncorretaException.class)
    public ResponseEntity<ErroRespostaDTO> manipularSenhaIncorreta(SenhaIncorretaException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // EquipeNaoEncontradaException
    @ExceptionHandler(EquipeNaoEncontradaException.class)
    public ResponseEntity<ErroRespostaDTO> manipularEquipeNaoEncontrada(EquipeNaoEncontradaException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // EquipeSemInformacaoException
    @ExceptionHandler(EquipeSemInformacaoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularEquipeSemInformacao(EquipeSemInformacaoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(NomeDeEquipeJaExisteException.class)
    public ResponseEntity<ErroRespostaDTO> manipularNomeDeEquipeJaExiste(NomeDeEquipeJaExisteException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro); // 409 Conflict
    }

    @ExceptionHandler(AcessoNaoAutorizadoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularAcessoNaoAutorizado(AcessoNaoAutorizadoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro); // 403 Forbidden
    }

    @ExceptionHandler(CriadorNaoPodeSairException.class)
    public ResponseEntity<ErroRespostaDTO> manipularCriadorNaoPodeSair(CriadorNaoPodeSairException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(UsuarioNaoEMembroException.class)
    public ResponseEntity<ErroRespostaDTO> manipularUsuarioNaoEMembro(UsuarioNaoEMembroException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Quando o limite do multipart do servidor é excedido (antes do controller)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErroRespostaDTO> manipularUploadExcedido(MaxUploadSizeExceededException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), "O tamanho do arquivo excede o limite permitido.");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(erro);
    }
}