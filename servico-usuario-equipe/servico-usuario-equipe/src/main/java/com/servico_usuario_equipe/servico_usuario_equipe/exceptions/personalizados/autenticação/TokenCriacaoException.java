package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação;

import lombok.Getter;

@Getter
public class TokenCriacaoException extends RuntimeException {
    private String mensagem;

    public TokenCriacaoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
