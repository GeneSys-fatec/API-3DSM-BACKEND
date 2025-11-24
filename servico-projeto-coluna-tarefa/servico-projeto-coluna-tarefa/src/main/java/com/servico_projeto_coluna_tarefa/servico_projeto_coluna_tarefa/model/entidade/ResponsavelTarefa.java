package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsavelTarefa {
    private String usuId;
    private String usuNome;
}