package com.servico_metricas.servico_metricas.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class TarefaDTO {

    private String tarId;

    private String tarStatus;

    private Boolean concluidaNoPrazo;

    private String tarDataConclusao;

    private List<ResponsavelTarefaDTO> responsaveis;
}