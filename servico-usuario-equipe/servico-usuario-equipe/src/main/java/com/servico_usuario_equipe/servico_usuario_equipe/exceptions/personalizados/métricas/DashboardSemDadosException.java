package com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.métricas;

import lombok.Getter;

@Getter
public class DashboardSemDadosException extends RuntimeException {
    private String mensagem;

    public DashboardSemDadosException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}