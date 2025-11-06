package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ProjetoDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import org.springframework.stereotype.Component;

@Component
public class ProjetoConverter {

    public ProjetoModel dtoParaModel(ProjetoDTO dto) {
        ProjetoModel model = new ProjetoModel();
        model.setProjId(dto.getProjId());
        model.setProjNome(dto.getProjNome());
        model.setProjDescricao(dto.getProjDescricao());
        model.setProjStatus(dto.getProjStatus());

        model.setEquipeId(dto.getEquId());

        return model;
    }

    public ProjetoDTO modelParaDto(ProjetoModel model) {
        ProjetoDTO dto = new ProjetoDTO();
        dto.setProjId(model.getProjId());
        dto.setProjNome(model.getProjNome());
        dto.setProjDescricao(model.getProjDescricao());
        dto.setProjStatus(model.getProjStatus());

        dto.setProjDataCriacao(model.getProjDataCriacao());
        dto.setProjDataAtualizacao(model.getProjDataAtualizacao());

        if (model.getEquipeId() != null) {
            dto.setEquId(model.getEquipeId());
        }

        return dto;
    }
}