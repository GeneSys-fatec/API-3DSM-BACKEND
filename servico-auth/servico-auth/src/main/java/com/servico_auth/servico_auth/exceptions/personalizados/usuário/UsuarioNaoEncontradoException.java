package com.servico_auth.servico_auth.exceptions.personalizados.usuário;

import lombok.Getter;

@Getter
public class UsuarioNaoEncontradoException extends RuntimeException {
    private String mensagem;

    public UsuarioNaoEncontradoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
