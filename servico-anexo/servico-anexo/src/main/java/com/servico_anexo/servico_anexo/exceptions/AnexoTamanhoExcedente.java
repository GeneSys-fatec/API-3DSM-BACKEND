package com.servico_anexo.servico_anexo.exceptions;

public class AnexoTamanhoExcedente extends RuntimeException {
    private final String mensagem;

    public AnexoTamanhoExcedente(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
