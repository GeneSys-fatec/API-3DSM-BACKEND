package com.servico_auditoria.servico_auditoria.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsavelAlteracaoDto {
    private String usuId;
    private String usuNome;
    private String usuEmail;
}