package com.servico_notificacao.servico_notificacao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_notificacao.servico_notificacao.model.NotificacaoModel;
import com.servico_notificacao.servico_notificacao.repository.NotificacaoRepository;

@Service
public class MarcaComoLidaNotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    public NotificacaoModel marcarComoLida(String id) {
        Optional<NotificacaoModel> notificacaoOpt = repository.findById(id);

        if (notificacaoOpt.isPresent()) {
            NotificacaoModel notificacao = notificacaoOpt.get();
            notificacao.setNotLida(true);
            return repository.save(notificacao);
        }

        return null;
    }

    public void marcarTodasComoLidas(String usuarioId) {
        List<NotificacaoModel> naoLidas = repository.findByNotUsuarioIdAndNotLidaFalse(usuarioId);

        for (NotificacaoModel notificacao : naoLidas) {
            notificacao.setNotLida(true);
        }

        repository.saveAll(naoLidas);
    }
}
