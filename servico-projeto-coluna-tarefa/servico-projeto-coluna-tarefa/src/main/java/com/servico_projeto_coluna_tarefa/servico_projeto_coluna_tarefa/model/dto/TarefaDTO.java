package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto;

import lombok.*;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;


@Data
public class TarefaDTO extends RepresentationModel<TarefaDTO> {
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

    private List<ResponsavelTarefaDTO> responsaveis;

    private String projId;


    private String googleId;
}