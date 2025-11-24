package com.servico_notificacao.servico_notificacao.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_notificacao.servico_notificacao.model.NotificacaoModel;
import com.servico_notificacao.servico_notificacao.repository.NotificacaoRepository;

@Service
public class BuscaNotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    public List<NotificacaoModel> listarTodas() {
        return repository.findAll();
    }

    public Optional<NotificacaoModel> listarPorId(String id) {
        return repository.findById(id);
    }

    public List<NotificacaoModel> listarPorUsuario(String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) {

            // --- ADICIONE ESTE TESTE DE LOG ---
            System.out.println("\n--- DEBUG NOTIFICAÇÃO [SERVICE] ---");
            System.out.println("ID do usuário chegou NULO ou VAZIO.");
            System.out.println("-------------------------------------\n");
            // --- FIM DO TESTE ---

            return Collections.emptyList();
        }
        return repository.findByNotUsuarioIdOrderByNotDataCriacaoDesc(usuarioId);
    }
}

