package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.tarefas;

import lombok.Getter;

@Getter
public class InvalidTaskDataException extends RuntimeException {
    private String mensagem;

    public InvalidTaskDataException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
