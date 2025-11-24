package com.servico_auth.servico_auth.exceptions.personalizados.usuário;

import lombok.Getter;

@Getter
public class EmailJaCadastradoException extends RuntimeException {
    private String mensagem;

    public EmailJaCadastradoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
