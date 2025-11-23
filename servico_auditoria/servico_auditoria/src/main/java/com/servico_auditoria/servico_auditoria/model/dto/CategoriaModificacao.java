package com.servico_auditoria.servico_auditoria.model.dto;

public enum CategoriaModificacao {
    EDICAO("Edição"),
    CRIACAO("Criação"),
    EXCLUSAO("Exclusão");

    private final String categoria;

    CategoriaModificacao(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }
}
