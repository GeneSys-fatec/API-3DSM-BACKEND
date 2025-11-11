package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.comentário;

import lombok.Getter;

@Getter
public class ComentarioEmBrancoException extends RuntimeException {
    private String mensagem;
    public ComentarioEmBrancoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
