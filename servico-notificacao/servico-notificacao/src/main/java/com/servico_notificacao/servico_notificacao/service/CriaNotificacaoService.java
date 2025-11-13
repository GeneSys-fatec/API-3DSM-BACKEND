package com.servico_notificacao.servico_notificacao.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_notificacao.servico_notificacao.model.NotificacaoModel;
import com.servico_notificacao.servico_notificacao.repository.NotificacaoRepository;
import com.servico_notificacao.servico_notificacao.client.UsuarioClient;
import com.servico_notificacao.servico_notificacao.model.dto.UsuarioDTO;

@Service
public class CriaNotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private ValidaNotificacaoService validaNotificacaoService;

    @Autowired
    private UsuarioClient usuarioClient;

    public NotificacaoModel criarNotificacaoAtribuicao(String usuarioOrigemId, String usuarioDestinoId, String nomeCriador, String tarefaId, String nomeTarefa) {
        UsuarioDTO origem = usuarioClient.getUsuario(usuarioOrigemId);
        if (origem != null && (nomeCriador == null || nomeCriador.isBlank())) {
            nomeCriador = origem.getUsuNome();
        }
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTarefaId(tarefaId);
        notificacao.setNotTipo("ATRIBUICAO");
        notificacao.setNotMensagem("A tarefa " + nomeTarefa + " foi atribuída a você por " + nomeCriador + ".");
        return validaNotificacaoService.salvarSeValido(notificacao, usuarioOrigemId);
    }

    public NotificacaoModel criarNotificacaoEdicaoTarefa(String usuarioOrigemId, String usuarioDestinoId, String tarefaId, String tituloTarefa, String nomeEditor) {
        UsuarioDTO editor = usuarioClient.getUsuario(usuarioOrigemId);
        if (editor != null && (nomeEditor == null || nomeEditor.isBlank())) {
            nomeEditor = editor.getUsuNome();
        }
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTarefaId(tarefaId);
        notificacao.setNotTipo("TAREFA_EDITADA");
        notificacao.setNotMensagem("A tarefa '" + tituloTarefa + "' foi editada por " + nomeEditor + ".");
        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return validaNotificacaoService.salvarSeValido(notificacao, usuarioOrigemId);
    }

    public NotificacaoModel criarNotificacaoAdicaoEquipe(String usuarioOrigemId, String usuarioDestinoId, String nomeEquipe, String nomeResponsavel) {
        UsuarioDTO responsavel = usuarioClient.getUsuario(usuarioOrigemId);
        if (responsavel != null && (nomeResponsavel == null || nomeResponsavel.isBlank())) {
            nomeResponsavel = responsavel.getUsuNome();
        }
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTipo("EQUIPE_ADICIONADA");
        notificacao.setNotMensagem("Você foi adicionado à equipe '" + nomeEquipe + "' por " + nomeResponsavel   + ".");
        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return validaNotificacaoService.salvarSeValido(notificacao, usuarioOrigemId);
    }

    public NotificacaoModel criarNotificacaoRemocaoEquipe(String usuarioOrigemId, String usuarioDestinoId, String nomeEquipe, String nomeResponsavel) {
        UsuarioDTO responsavel = usuarioClient.getUsuario(usuarioOrigemId);
        if (responsavel != null && (nomeResponsavel == null || nomeResponsavel.isBlank())) {
            nomeResponsavel = responsavel.getUsuNome();
        }
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTipo("EQUIPE_REMOVIDA");
        notificacao.setNotMensagem("Você foi removido da equipe '" + nomeEquipe + "' por " + nomeResponsavel    + ".");
        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return validaNotificacaoService.salvarSeValido(notificacao, usuarioOrigemId);
    }

    public NotificacaoModel criarNotificacaoComentario(String usuarioOrigemId, String usuarioDestinoId, String nomeComentador, String tarefaId) {
        UsuarioDTO comentador = usuarioClient.getUsuario(usuarioOrigemId);
        if (comentador != null && (nomeComentador == null || nomeComentador.isBlank())) {
            nomeComentador = comentador.getUsuNome();
        }
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTarefaId(tarefaId);
        notificacao.setNotTipo("COMENTARIO");
        notificacao.setNotMensagem(nomeComentador + " comentou em uma tarefa atribuída a você.");
        return validaNotificacaoService.salvarSeValido(notificacao, usuarioOrigemId);
    }

    public NotificacaoModel criarNotificacaoPrazo(String tarefaId, String tituloTarefa,  String usuarioDestinoId ) {
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTipo("PRAZO");
        notificacao.setNotMensagem("A tarefa '" + tituloTarefa + "' está próxima do prazo máximo!");
        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return repository.save(notificacao);
    }

    public NotificacaoModel criarNotificacaoPrazoExpirado(String tarefaId, String tituloTarefa, String usuarioDestinoId) {
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTarefaId(tarefaId);
        notificacao.setNotTipo("PRAZO_EXPIRADO");
        notificacao.setNotMensagem("A tarefa '" + tituloTarefa + "' está com o prazo expirado!");
        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return repository.save(notificacao);
    }
}
