package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    public TarefaModel atualizarTarefa(String tarId, TarefaDTO dto, UsuarioDTO editor) {
        TarefaModel tarefa = buscaTarefaService.buscarPorId(tarId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + tarId));

        Set<String> responsaveisAntigos = tarefa.getResponsaveis().stream()
                .map(ResponsavelTarefa::getUsuId)
                .collect(Collectors.toSet());

        System.out.println("\n[EditaTarefaService] Responsáveis Antigos: " + responsaveisAntigos);
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
                System.out.println("[EditaTarefaService] Editor da Tarefa: " + editor.getUsuNome());

                for (ResponsavelTarefa novoResponsavel : responsaveisNovosEntidade) {
                    System.out.println("[EditaTarefaService] A verificar Novo Responsável: " + novoResponsavel.getUsuNome());

                    if (!responsaveisAntigos.contains(novoResponsavel.getUsuId()) &&
                            !novoResponsavel.getUsuId().equals(editor.getUsuId())) {

                        System.out.println("[EditaTarefaService] DETETADA NOVA ATRIBUIÇÃO! A enviar notificação para: " + novoResponsavel.getUsuNome());

                        NotificacaoRequestDTO notificacaoDTO = new NotificacaoRequestDTO(
                                editor.getUsuId(),
                                novoResponsavel.getUsuId(),
                                editor.getUsuNome(),
                                tarefaAtualizada.getTarId(),
                                tarefaAtualizada.getTarTitulo()
                        );

                        // Chamada corrigida (sem authHeader)
                        notificacaoClient.criarNotificacaoAtribuicao(notificacaoDTO);
                    }
                }
            } catch (Exception e) {
                System.err.println("AVISO: Falha ao enviar notificação de atribuição. Erro: " + e.getMessage());
            }
        }

        return tarefaAtualizada;
    }

    public TarefaModel atualizar(TarefaModel tarefa) {
        return tarefaRepository.save(tarefa);
    }

    private String getAuthHeader() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getHeader("Authorization");
        } catch (Exception e) {
            System.err.println("Não foi possível obter o cabeçalho de Autorização (pode ser uma tarefa de sistema).");
            return null;
        }
    }
}