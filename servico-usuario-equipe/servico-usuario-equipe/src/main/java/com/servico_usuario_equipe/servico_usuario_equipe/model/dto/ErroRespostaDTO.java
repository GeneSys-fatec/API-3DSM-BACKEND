package com.servico_usuario_equipe.servico_usuario_equipe.model.dto;

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
