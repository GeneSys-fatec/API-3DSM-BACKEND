package com.servico_auth.servico_auth.model.dto.resposta;

public record ResponseDTO (
    String usuId, 
    String usuNome, 
    String usuEmail, 
    String usuCaminhoFoto
) {}
