package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.equipes;

import lombok.Getter;

@Getter
public class EquipeSemInformacaoException extends RuntimeException {
    private String mensagem;

    public EquipeSemInformacaoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}