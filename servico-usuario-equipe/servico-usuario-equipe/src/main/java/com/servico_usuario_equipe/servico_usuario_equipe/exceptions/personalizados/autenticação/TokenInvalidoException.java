package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação;

import lombok.Getter;

@Getter
public class TokenInvalidoException extends RuntimeException {
    private String mensagem;

    public TokenInvalidoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
