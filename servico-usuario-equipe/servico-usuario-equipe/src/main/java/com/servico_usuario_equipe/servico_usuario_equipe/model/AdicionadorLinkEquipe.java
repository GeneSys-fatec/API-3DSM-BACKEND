package com.servico_usuario_equipe.servico_usuario_equipe.model;

import com.servico_usuario_equipe.servico_usuario_equipe.controller.Equipe.BuscaEquipeController;
import com.servico_usuario_equipe.servico_usuario_equipe.controller.Equipe.CriaEquipeController;
import com.servico_usuario_equipe.servico_usuario_equipe.controller.Equipe.EditaEquipeController;
import com.servico_usuario_equipe.servico_usuario_equipe.controller.Equipe.ExcluiEquipeController;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.EquipeDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AdicionadorLinkEquipe {

    public void adicionarLink(EquipeDTO dto) {
        String id = dto.getEquId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaEquipeController.class).getEquipeById(id))
                .withSelfRel();

        Link cadastrarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(CriaEquipeController.class).cadastrarEquipe(dto, null))
                .withRel("cadastrar");

        Link listarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaEquipeController.class).listarEquipes())
                .withRel("listar");

        Link atualizarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EditaEquipeController.class).atualizarEquipe(id, dto, null))
                .withRel("atualizar");

        Link apagarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ExcluiEquipeController.class).apagarEquipe(id, null))
                .withRel("excluir");

        dto.add(selfLink, cadastrarLink, listarLink, atualizarLink, apagarLink);
    }

    public void adicionarLink(List<EquipeDTO> lista) {
        for (EquipeDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}