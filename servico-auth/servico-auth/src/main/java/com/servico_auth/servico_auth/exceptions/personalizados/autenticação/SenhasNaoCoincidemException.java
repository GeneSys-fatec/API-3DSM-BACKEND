package com.servico_auth.servico_auth.exceptions.personalizados.autenticação;

import lombok.Getter;

@Getter
public class SenhasNaoCoincidemException extends RuntimeException {
    private String mensagem;

    public SenhasNaoCoincidemException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
