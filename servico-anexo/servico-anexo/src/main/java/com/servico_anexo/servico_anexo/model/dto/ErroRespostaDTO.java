package com.servico_anexo.servico_anexo.model.dto;

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