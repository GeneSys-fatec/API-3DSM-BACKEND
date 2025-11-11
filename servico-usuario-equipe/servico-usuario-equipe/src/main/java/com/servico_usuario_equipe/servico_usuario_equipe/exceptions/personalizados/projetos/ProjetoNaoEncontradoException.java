package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.projetos;

import lombok.Getter;

@Getter
public class ProjetoNaoEncontradoException extends RuntimeException {
    private String mensagem;

    public ProjetoNaoEncontradoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
