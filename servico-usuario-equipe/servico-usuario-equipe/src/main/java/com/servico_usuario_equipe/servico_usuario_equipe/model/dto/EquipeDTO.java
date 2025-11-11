package com.servico_usuario_equipe.servico_usuario_equipe.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.usuario.UsuarioDTO;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

@Data
public class EquipeDTO extends RepresentationModel<EquipeDTO> {

    private String equId;
    private String equNome;
    private String equDescricao;
    private String equDataCriacao;
    private String equDataAtualizacao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UsuarioDTO> equMembros;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProjetoDTO> projetos; // Campo para carregar os projetos da equipe

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> membrosEmails;
}