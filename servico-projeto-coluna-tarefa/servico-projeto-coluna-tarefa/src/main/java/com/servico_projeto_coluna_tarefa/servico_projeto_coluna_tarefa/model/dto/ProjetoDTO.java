package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
public class ProjetoDTO extends RepresentationModel<ProjetoDTO> {
    private String projId;
    private String projNome;
    private String projDescricao;
    private String projStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String projDataCriacao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String projDataAtualizacao;

    private String equId;

}