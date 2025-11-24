package com.servico_anexo.servico_anexo.model;

import lombok.Data;

@Data
public class AnexoTarefaModel {
    private String arquivoNome;
    private String arquivoTipo;
    private long arquivoTamanho;
    private String arquivoCaminho;
}
