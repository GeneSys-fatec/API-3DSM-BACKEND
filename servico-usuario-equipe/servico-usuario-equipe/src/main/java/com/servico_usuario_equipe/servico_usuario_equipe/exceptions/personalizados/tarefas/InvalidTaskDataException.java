package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.tarefas;

import lombok.Getter;

@Getter
public class InvalidTaskDataException extends RuntimeException {
    private String mensagem;

    public InvalidTaskDataException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
