package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação;

import lombok.Getter;

@Getter
public class CredenciaisInvalidasException extends RuntimeException {
    private String mensagem;

    public CredenciaisInvalidasException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
