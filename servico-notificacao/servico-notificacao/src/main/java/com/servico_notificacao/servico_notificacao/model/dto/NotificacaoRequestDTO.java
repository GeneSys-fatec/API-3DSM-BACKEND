package com.servico_notificacao.servico_notificacao.model.dto;

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