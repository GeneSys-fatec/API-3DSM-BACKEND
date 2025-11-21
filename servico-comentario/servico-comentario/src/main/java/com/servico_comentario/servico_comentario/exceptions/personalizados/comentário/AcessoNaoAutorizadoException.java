package com.servico_comentario.servico_comentario.exceptions.personalizados.comentário;

import lombok.Getter;

@Getter
public class AcessoNaoAutorizadoException extends RuntimeException {
    private String mensagem;
    public AcessoNaoAutorizadoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}