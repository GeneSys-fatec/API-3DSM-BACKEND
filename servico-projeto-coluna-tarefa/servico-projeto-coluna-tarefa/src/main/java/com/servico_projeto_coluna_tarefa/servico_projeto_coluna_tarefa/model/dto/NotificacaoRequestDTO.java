package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoRequestDTO {
    private String usuarioOrigemId;
    private String usuarioDestinoId;
    private String nomeCriador;
    private String tarefaId;
    private String nomeTarefa;
}