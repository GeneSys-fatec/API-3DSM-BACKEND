package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação;

import lombok.Getter;

@Getter
public class SenhaIncorretaException extends RuntimeException {
    private String mensagem;

    public SenhaIncorretaException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
