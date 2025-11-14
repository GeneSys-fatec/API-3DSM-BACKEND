package com.servico_metricas.servico_metricas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErroRespostaDTO {
    private String titulo;
    private String mensagem;
}
