package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Tarefas")
public class TarefaModel {
    @Id
    private String tarId;
    private String tarTitulo;
    private String tarDescricao;
    private String tarPrazo;
    private String tarStatus;
    private String tarPrioridade;

    private List<String> anexoIds;

    private String tarDataCriacao;
    private String tarDataAtualizacao;
    private String tarDataConclusao;
    private Boolean concluidaNoPrazo;

    private List<ResponsavelTarefa> responsaveis;

    private String projId;

    private String googleId;
}