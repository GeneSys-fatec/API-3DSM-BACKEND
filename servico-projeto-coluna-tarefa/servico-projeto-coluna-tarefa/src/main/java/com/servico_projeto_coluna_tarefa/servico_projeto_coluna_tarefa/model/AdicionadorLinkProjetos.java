package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto.BuscaProjetoController;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto.CriaProjetoController;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto.EditaProjetoController;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.controller.Projeto.ExcluiProjetoController;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ProjetoDTO;

@Component
public class AdicionadorLinkProjetos {

    public void adicionarLink(ProjetoDTO dto) {

        String id = dto.getProjId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaProjetoController.class).buscarPorId(id, null))
                .withSelfRel();


        Link cadastrarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(CriaProjetoController.class).cadastrarProjeto(dto,
                        null))
                .withRel("cadastrar");

        Link allLink = WebMvcLinkBuilder

                .linkTo(WebMvcLinkBuilder.methodOn(BuscaProjetoController.class).listarTodas())
                .withRel("listar");

        Link updateLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EditaProjetoController.class).atualizar(id, dto,
                        null))
                .withRel("atualizar");

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ExcluiProjetoController.class).apagarProjeto(id,
                        null))
                .withRel("apagar");

        dto.add(selfLink, cadastrarLink, allLink, updateLink, deleteLink);
    }

    public void adicionarLink(List<ProjetoDTO> lista) {
        for (ProjetoDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}