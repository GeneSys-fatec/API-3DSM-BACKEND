package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.tarefas;

import lombok.Getter;

@Getter
public class AnexoTamanhoExcedente extends RuntimeException {
    private String mensagem;

    public AnexoTamanhoExcedente(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
