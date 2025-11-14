package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.usuário;

import lombok.Getter;

@Getter
public class UsuarioNaoEncontradoException extends RuntimeException {
    private String mensagem;

    public UsuarioNaoEncontradoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
