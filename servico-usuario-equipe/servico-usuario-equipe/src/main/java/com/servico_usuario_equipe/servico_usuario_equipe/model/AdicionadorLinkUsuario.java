package com.servico_usuario_equipe.servico_usuario_equipe.model;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.servico_usuario_equipe.servico_usuario_equipe.controller.Usuario.BuscaUsuarioController;
import com.servico_usuario_equipe.servico_usuario_equipe.controller.Usuario.EditaUsuarioController;
import com.servico_usuario_equipe.servico_usuario_equipe.controller.Usuario.ExcluiUsuarioController;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.usuario.UsuarioDTO;

@Component
public class AdicionadorLinkUsuario {
    public void adicionarLink(UsuarioDTO dto) {
        String id = dto.getUsuId();

        Link selfLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(BuscaUsuarioController.class).buscarPorId(id))
            .withSelfRel();

        Link allLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(BuscaUsuarioController.class).listarUsuario())
            .withRel("listar");

        Link updateLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(EditaUsuarioController.class).atualizarNomeEmail(id, dto, null))
            .withRel("atualizar");

        Link deleteLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(ExcluiUsuarioController.class).apagarUsuario(id))
            .withRel("excluir");

        dto.add(selfLink, allLink, updateLink, deleteLink);
    }

    public void adicionarLink(List<UsuarioDTO> lista) {
        for (UsuarioDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}
