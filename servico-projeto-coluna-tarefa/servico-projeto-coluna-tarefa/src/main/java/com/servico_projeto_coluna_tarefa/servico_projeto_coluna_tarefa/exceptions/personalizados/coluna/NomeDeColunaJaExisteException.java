package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.coluna;

public class NomeDeColunaJaExisteException extends RuntimeException {
    private final String mensagem;

    public NomeDeColunaJaExisteException(String message, String mensagem) {
        super(message);
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}