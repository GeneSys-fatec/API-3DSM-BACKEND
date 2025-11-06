package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColunaDTO extends RepresentationModel<ColunaDTO> {
    private String Id;
    private String Titulo;
    private Integer Ordem;
    private String projId;
}