package com.servico_usuario_equipe.servico_usuario_equipe.model.dto;

public record ResponseDTO (
    String usuId, 
    String usuNome, 
    String usuEmail, 
    String usuCaminhoFoto
) {}
