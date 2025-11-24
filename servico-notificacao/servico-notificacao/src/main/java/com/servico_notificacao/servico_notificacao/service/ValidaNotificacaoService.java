package com.servico_notificacao.servico_notificacao.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_notificacao.servico_notificacao.repository.NotificacaoRepository;
import com.servico_notificacao.servico_notificacao.model.NotificacaoModel;

@Service
public class ValidaNotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    public NotificacaoModel salvarSeValido(NotificacaoModel notificacao, String usuarioOrigemId) {
        if (notificacao.getNotUsuarioId().equals(usuarioOrigemId)) {
            return null;
        }

        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return repository.save(notificacao);
    }
}
