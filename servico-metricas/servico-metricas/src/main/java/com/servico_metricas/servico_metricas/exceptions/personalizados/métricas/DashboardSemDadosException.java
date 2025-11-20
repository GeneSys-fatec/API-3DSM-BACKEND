package com.servico_metricas.servico_metricas.exceptions.personalizados.métricas;

import lombok.Getter;

@Getter
public class DashboardSemDadosException extends RuntimeException {
    private String mensagem;

    public DashboardSemDadosException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}