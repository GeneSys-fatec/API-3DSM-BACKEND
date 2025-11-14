package com.servico_comentario.servico_comentario.exceptions.personalizados.comentário;

import lombok.Getter;

@Getter
public class ConteudoInapropriadoException extends RuntimeException {
    private String mensagem;
    
    public ConteudoInapropriadoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
