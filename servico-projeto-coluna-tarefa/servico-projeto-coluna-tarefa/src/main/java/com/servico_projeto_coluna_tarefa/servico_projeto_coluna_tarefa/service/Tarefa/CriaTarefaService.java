package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        if ("Concluída".equalsIgnoreCase(tarefa.getTarStatus())) {
        }

        TarefaModel tarefaCriada = tarefaRepository.save(tarefa);

        System.out.println("\n[CriaTarefaService] Tarefa criada. A verificar notificações...");

        if (usuarioLogado != null) {
            try {
                System.out.println("[CriaTarefaService] Criador da Tarefa: " + usuarioLogado.getUsuNome());

                for (ResponsavelTarefa responsavel : responsaveisEntidade) {
                    System.out.println("[CriaTarefaService] A verificar responsável: " + responsavel.getUsuNome());

                    if (!responsavel.getUsuId().equals(usuarioLogado.getUsuId())) {

                        System.out.println("[CriaTarefaService] DETETADA NOVA ATRIBUIÇÃO! A enviar notificação para: " + responsavel.getUsuNome());

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
                System.err.println("AVISO: Falha ao enviar notificação de atribuição na criação da tarefa. Erro: " + e.getMessage());
            }
        }

        return tarefaCriada;
    }

    public String generateGoogleEventId() {
        String prefix = "taskmngr";
        int len = 22;
        char[] buf = new char[prefix.length() + len];

        for (int i = 0; i < prefix.length(); i++)
            buf[i] = prefix.charAt(i);
        for (int i = prefix.length(); i < buf.length; i++) {
            buf[i] = BASE32HEX[RNG.nextInt(BASE32HEX.length)];
        }
        return new String(buf);
    }

}