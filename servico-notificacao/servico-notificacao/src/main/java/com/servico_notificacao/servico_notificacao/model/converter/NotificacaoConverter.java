package com.servico_notificacao.servico_notificacao.model.converter;

import org.springframework.stereotype.Service;

import com.servico_notificacao.servico_notificacao.model.dto.NotificacaoDTO;
import com.servico_notificacao.servico_notificacao.model.NotificacaoModel;

@Service
public class NotificacaoConverter {
    public static NotificacaoDTO modelParaDto(NotificacaoModel model) {
        NotificacaoDTO dto = new NotificacaoDTO();
        dto.setNotId(model.getNotId());
        dto.setNotMensagem(model.getNotMensagem());
        dto.setNotTipo(model.getNotTipo());
        dto.setNotLida(model.isNotLida());
        dto.setNotDataCriacao(model.getNotDataCriacao());
        return dto;
    }

    public static NotificacaoModel dtoParaModel(NotificacaoDTO dto) {
        NotificacaoModel model = new NotificacaoModel();
        model.setNotId(dto.getNotId());
        model.setNotMensagem(dto.getNotMensagem());
        model.setNotTipo(dto.getNotTipo());
        model.setNotLida(dto.isNotLida());
        model.setNotDataCriacao(dto.getNotDataCriacao());
        return model;
    }
}
