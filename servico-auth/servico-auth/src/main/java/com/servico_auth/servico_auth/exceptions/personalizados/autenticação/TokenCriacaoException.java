package com.servico_auth.servico_auth.exceptions.personalizados.autenticação;

import lombok.Getter;

@Getter
public class TokenCriacaoException extends RuntimeException {
    private String mensagem;

    public TokenCriacaoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
