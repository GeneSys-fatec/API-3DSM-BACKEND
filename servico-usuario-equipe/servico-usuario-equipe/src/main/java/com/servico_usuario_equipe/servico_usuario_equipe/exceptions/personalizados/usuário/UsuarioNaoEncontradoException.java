package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.usuário;

import lombok.Getter;

@Getter
public class UsuarioNaoEncontradoException extends RuntimeException {
    private String mensagem;

    public UsuarioNaoEncontradoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
