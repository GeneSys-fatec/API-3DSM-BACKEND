package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter;

import org.springframework.stereotype.Component;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ColunaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ColunaModel;

@Component
public class ColunaConverter {
    public ColunaModel dtoParaModel(ColunaDTO dto) {
        if (dto == null)
            return null;

        ColunaModel model = new ColunaModel();
        model.setColTitulo(dto.getTitulo());
        model.setProjId(dto.getProjId());

        return model;
    }

    public ColunaDTO modelParaDto(ColunaModel model) {
        if (model == null)
            return null;

        return new ColunaDTO(
                model.getColId(),
                model.getColTitulo(),
                model.getColOrdem(),
                model.getProjId());
    }
}
