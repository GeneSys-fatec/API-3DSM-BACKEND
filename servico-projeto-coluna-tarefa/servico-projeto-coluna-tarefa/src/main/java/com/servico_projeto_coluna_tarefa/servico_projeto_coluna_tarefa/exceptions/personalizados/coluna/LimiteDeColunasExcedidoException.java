package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.coluna;

public class LimiteDeColunasExcedidoException extends RuntimeException {
    private final String mensagem;

    public LimiteDeColunasExcedidoException(String message, String mensagem) {
        super(message);
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}