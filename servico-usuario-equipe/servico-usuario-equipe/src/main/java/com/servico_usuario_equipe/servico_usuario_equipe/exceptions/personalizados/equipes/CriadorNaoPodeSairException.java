package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes;

import lombok.Getter;

@Getter
public class CriadorNaoPodeSairException extends RuntimeException {
    private String mensagem;
    public CriadorNaoPodeSairException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}