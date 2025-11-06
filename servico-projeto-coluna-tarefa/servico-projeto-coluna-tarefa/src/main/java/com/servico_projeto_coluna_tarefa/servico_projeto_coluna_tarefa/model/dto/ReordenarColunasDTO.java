package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto;

import lombok.Data;

import java.util.List;


@Data
public class ReordenarColunasDTO {
    private String projetoId;
    private List<String> colunasIdsOrdenadas;
}