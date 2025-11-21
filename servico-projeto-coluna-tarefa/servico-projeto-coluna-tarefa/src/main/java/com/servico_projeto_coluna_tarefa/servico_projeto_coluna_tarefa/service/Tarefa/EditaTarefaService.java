package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client.AuditoriaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ResponsavelTarefa;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client.NotificacaoClient;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.client.UsuarioClient;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.NotificacaoRequestDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;

@Service
public class EditaTarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private BuscaTarefaService buscaTarefaService;

    @Autowired
    private NotificacaoClient notificacaoClient;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private AuditoriaClient auditoriaClient;

    public TarefaModel atualizarTarefa(String tarId, TarefaDTO dto, UsuarioDTO editor) {
        TarefaModel tarefa = buscaTarefaService.buscarPorId(tarId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + tarId));

        String tituloAntigo = tarefa.getTarTitulo();
        String descAntiga = tarefa.getTarDescricao();
        String statusAntigo = tarefa.getTarStatus();
        String prioridadeAntiga = tarefa.getTarPrioridade();
        String prazoAntigo = tarefa.getTarPrazo();

        Set<String> responsaveisAntigosIds = tarefa.getResponsaveis().stream()
                .map(ResponsavelTarefa::getUsuId)
                .collect(Collectors.toSet());

        tarefa.setTarTitulo(dto.getTarTitulo());
        tarefa.setTarDescricao(dto.getTarDescricao());
        tarefa.setTarStatus(dto.getTarStatus());
        tarefa.setTarPrioridade(dto.getTarPrioridade());
        tarefa.setTarPrazo(dto.getTarPrazo());
        tarefa.setTarDataAtualizacao(dto.getTarDataAtualizacao());
        tarefa.setProjId(dto.getProjId());

        List<ResponsavelTarefa> responsaveisNovosEntidade;
        if (dto.getResponsaveis() != null && !dto.getResponsaveis().isEmpty()) {
            responsaveisNovosEntidade = dto.getResponsaveis().stream()
                    .map(dtoResp -> new ResponsavelTarefa(dtoResp.getUsuId(), dtoResp.getUsuNome()))
                    .collect(Collectors.toList());
            tarefa.setResponsaveis(responsaveisNovosEntidade);
        } else {
            tarefa.setResponsaveis(new ArrayList<>());
            responsaveisNovosEntidade = new ArrayList<>();
        }

        if ("Concluída".equalsIgnoreCase(dto.getTarStatus())) {
            String dataConclusao = java.time.LocalDate.now().toString();
            tarefa.setTarDataConclusao(dataConclusao);
            java.time.LocalDate prazo = java.time.LocalDate.parse(tarefa.getTarPrazo());
            java.time.LocalDate conclusao = java.time.LocalDate.parse(dataConclusao);
            tarefa.setConcluidaNoPrazo(!conclusao.isAfter(prazo));
        } else {
            tarefa.setTarDataConclusao(null);
            tarefa.setConcluidaNoPrazo(null);
        }

        TarefaModel tarefaAtualizada = tarefaRepository.save(tarefa);

        if (editor != null) {
            try {
                for (ResponsavelTarefa novoResponsavel : responsaveisNovosEntidade) {
                    if (!responsaveisAntigosIds.contains(novoResponsavel.getUsuId()) &&
                            !novoResponsavel.getUsuId().equals(editor.getUsuId())) {

                        NotificacaoRequestDTO notificacaoDTO = new NotificacaoRequestDTO(
                                editor.getUsuId(),
                                novoResponsavel.getUsuId(),
                                editor.getUsuNome(),
                                tarefaAtualizada.getTarId(),
                                tarefaAtualizada.getTarTitulo()
                        );
                        notificacaoClient.criarNotificacaoAtribuicao(notificacaoDTO);
                    }
                }
            } catch (Exception e) {
                System.err.println("AVISO: Falha ao enviar notificação: " + e.getMessage());
            }
        }

        try {
            List<AuditoriaClient.ModificacaoSimplesDTO> alteracoes = new ArrayList<>();

            if (!Objects.equals(tituloAntigo, tarefaAtualizada.getTarTitulo())) {
                alteracoes.add(new AuditoriaClient.ModificacaoSimplesDTO("EDICAO",
                        "Título alterado de '" + tituloAntigo + "' para '" + tarefaAtualizada.getTarTitulo() + "'"));
            }
            if (!Objects.equals(statusAntigo, tarefaAtualizada.getTarStatus())) {
                alteracoes.add(new AuditoriaClient.ModificacaoSimplesDTO("EDICAO",
                        "Status alterado de '" + statusAntigo + "' para '" + tarefaAtualizada.getTarStatus() + "'"));
            }
            if (!Objects.equals(prioridadeAntiga, tarefaAtualizada.getTarPrioridade())) {
                alteracoes.add(new AuditoriaClient.ModificacaoSimplesDTO("EDICAO",
                        "Prioridade alterada de '" + prioridadeAntiga + "' para '" + tarefaAtualizada.getTarPrioridade() + "'"));
            }
            if (!Objects.equals(prazoAntigo, tarefaAtualizada.getTarPrazo())) {
                alteracoes.add(new AuditoriaClient.ModificacaoSimplesDTO("EDICAO",
                        "Prazo alterado de " + prazoAntigo + " para " + tarefaAtualizada.getTarPrazo()));
            }
            if (!Objects.equals(descAntiga, tarefaAtualizada.getTarDescricao())) {
                alteracoes.add(new AuditoriaClient.ModificacaoSimplesDTO("EDICAO", "Descrição da tarefa atualizada"));
            }

            if (alteracoes.isEmpty()) {
                alteracoes.add(new AuditoriaClient.ModificacaoSimplesDTO("EDICAO", "Tarefa atualizada (outros campos)"));
            }

            AuditoriaClient.RegistrarLogRequest logRequest = new AuditoriaClient.RegistrarLogRequest(
                    tarefaAtualizada.getProjId(),
                    tarefaAtualizada.getTarId(),
                    tarefaAtualizada.getTarTitulo(),
                    editor != null ? editor.getUsuId() : "sistema",
                    editor != null ? editor.getUsuEmail() : "sistema@email.com",
                    alteracoes
            );

            auditoriaClient.registrarLog(logRequest,
                    editor != null ? editor.getUsuId() : null,
                    editor != null ? editor.getUsuEmail() : null,
                    editor != null ? editor.getUsuNome() : null
            );

        } catch (Exception e) {
            System.err.println("[EditaTarefaService] Erro auditoria: " + e.getMessage());
        }

        return tarefaAtualizada;
    }
}