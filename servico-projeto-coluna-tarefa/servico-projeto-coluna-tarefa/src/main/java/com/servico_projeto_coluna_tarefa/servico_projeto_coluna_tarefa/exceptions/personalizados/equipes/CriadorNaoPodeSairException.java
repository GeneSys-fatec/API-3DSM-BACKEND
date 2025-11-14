package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes;

import lombok.Getter;

@Getter
public class CriadorNaoPodeSairException extends RuntimeException {
    private String mensagem;
    public CriadorNaoPodeSairException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}