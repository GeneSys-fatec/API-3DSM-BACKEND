package com.servico_notificacao.servico_notificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_notificacao.servico_notificacao.repository.NotificacaoRepository;

@Service
public class ExcluiNotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    public void deletarNotificacao(String id) {
        repository.deleteById(id);
    }
}
