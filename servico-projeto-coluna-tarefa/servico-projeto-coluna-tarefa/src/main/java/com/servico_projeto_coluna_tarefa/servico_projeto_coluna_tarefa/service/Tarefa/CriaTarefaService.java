package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Tarefa;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.TarefaConverter;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.TarefaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ResponsavelTarefa;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.TarefaRepository;


@Service
public class CriaTarefaService {
    @Autowired
    private TarefaConverter tarefaConverter;


    @Autowired
    private TarefaRepository tarefaRepository;

    private static final char[] BASE32HEX = "0123456789abcdefghijklmnopqrstuv".toCharArray();
    private static final SecureRandom RNG = new SecureRandom();

    public TarefaModel criarTarefa(TarefaDTO dto) {
        TarefaModel tarefa = tarefaConverter.dtoParaModel(dto);

        if (dto.getResponsaveis() != null && !dto.getResponsaveis().isEmpty()) {
            List<ResponsavelTarefa> responsaveisEntidade = dto.getResponsaveis().stream()
                    .map(dtoResp -> new ResponsavelTarefa(dtoResp.getUsuId(), dtoResp.getUsuNome()))
                    .collect(Collectors.toList());
            tarefa.setResponsaveis(responsaveisEntidade);
        } else {
            tarefa.setResponsaveis(new ArrayList<>());
        }

        if (tarefa.getGoogleId() == null || tarefa.getGoogleId().isBlank()) {
            tarefa.setGoogleId(generateGoogleEventId());
        }

        if ("Concluída".equalsIgnoreCase(tarefa.getTarStatus())) {
            String dataConclusao = LocalDate.now().toString();
            tarefa.setTarDataConclusao(dataConclusao);

            try {
                LocalDate prazo = LocalDate.parse(tarefa.getTarPrazo());
                LocalDate conclusao = LocalDate.parse(dataConclusao);
                tarefa.setConcluidaNoPrazo(!conclusao.isAfter(prazo));
            } catch (DateTimeParseException e) {
                tarefa.setConcluidaNoPrazo(false);
            }
        }

        return tarefaRepository.save(tarefa);
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