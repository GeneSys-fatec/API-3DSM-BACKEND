package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "projetos")
public class ProjetoModel {
    @Id
    private String projId;
    private String projNome;
    private String projDescricao;
    private String projStatus;
    private String projDataCriacao;
    private String projDataAtualizacao;

    private String equipeId;
}
