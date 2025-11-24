package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client.AuditoriaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client.NotificacaoClient;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client.UsuarioClient;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.NotificacaoRequestDTO;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.TarefaConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ResponsavelTarefa;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;

@Service
public class CriaTarefaService {
    @Autowired
    private TarefaConverter tarefaConverter;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private NotificacaoClient notificacaoClient;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private AuditoriaClient auditoriaClient;

    private static final char[] BASE32HEX = "0123456789abcdefghijklmnopqrstuv".toCharArray();
    private static final SecureRandom RNG = new SecureRandom();

    public TarefaModel criarTarefa(TarefaDTO dto, UsuarioDTO usuarioLogado) {
        TarefaModel tarefa = tarefaConverter.dtoParaModel(dto);

        List<ResponsavelTarefa> responsaveisEntidade;

        if (dto.getResponsaveis() != null && !dto.getResponsaveis().isEmpty()) {
            responsaveisEntidade = dto.getResponsaveis().stream()
                    .map(dtoResp -> new ResponsavelTarefa(dtoResp.getUsuId(), dtoResp.getUsuNome()))
                    .collect(Collectors.toList());
            tarefa.setResponsaveis(responsaveisEntidade);
        } else {
            tarefa.setResponsaveis(new ArrayList<>());
            responsaveisEntidade = new ArrayList<>();
        }

        if (tarefa.getGoogleId() == null || tarefa.getGoogleId().isBlank()) {
            tarefa.setGoogleId(generateGoogleEventId());
        }

        // Save
        TarefaModel tarefaCriada = tarefaRepository.save(tarefa);

        // Notificações
        if (usuarioLogado != null) {
            try {
                for (ResponsavelTarefa responsavel : responsaveisEntidade) {
                    if (!responsavel.getUsuId().equals(usuarioLogado.getUsuId())) {
                        NotificacaoRequestDTO notificacaoDTO = new NotificacaoRequestDTO(
                                usuarioLogado.getUsuId(),
                                responsavel.getUsuId(),
                                usuarioLogado.getUsuNome(),
                                tarefaCriada.getTarId(),
                                tarefaCriada.getTarTitulo()
                        );
                        notificacaoClient.criarNotificacaoAtribuicao(notificacaoDTO);
                    }
                }
            } catch (Exception e) {
                System.err.println("AVISO: Falha ao enviar notificação: " + e.getMessage());
            }
        }

        // LOG DE AUDITORIA
        try {
            String userId = usuarioLogado != null ? usuarioLogado.getUsuId() : "sistema";
            String userEmail = usuarioLogado != null ? usuarioLogado.getUsuEmail() : "sistema@email.com";
            String userName = usuarioLogado != null ? usuarioLogado.getUsuNome() : "Sistema";

            AuditoriaClient.RegistrarLogRequest logRequest = new AuditoriaClient.RegistrarLogRequest(
                    tarefaCriada.getProjId(),
                    tarefaCriada.getTarId(),
                    tarefaCriada.getTarTitulo(),
                    userId,
                    userEmail,
                    List.of(new AuditoriaClient.ModificacaoSimplesDTO("CRIACAO", "Tarefa criada"))
            );

            auditoriaClient.registrarLog(logRequest, userId, userEmail, userName);
        } catch (Exception e) {
            System.err.println("Erro auditoria criação: " + e.getMessage());
        }

        return tarefaCriada;
    }

    public String generateGoogleEventId() {
        String prefix = "taskmngr";
        int len = 22;
        char[] buf = new char[prefix.length() + len];
        for (int i = 0; i < prefix.length(); i++) buf[i] = prefix.charAt(i);
        for (int i = prefix.length(); i < buf.length; i++) {
            buf[i] = BASE32HEX[RNG.nextInt(BASE32HEX.length)];
        }
        return new String(buf);
    }
}