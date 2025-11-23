package com.servico_auditoria.servico_auditoria.model.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String usuId;
    private String usuNome;
    private String usuEmail;
    private String usuCaminhoFoto;
    private String usuDataCriacao;
    private String usuDataAtualizacao;
}
