package com.servico_metricas.servico_metricas.service.Metrica;

import com.servico_metricas.servico_metricas.model.dto.TarefaDTO;
import com.servico_metricas.servico_metricas.model.dto.ResponsavelTarefaDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_metricas.servico_metricas.exceptions.personalizados.métricas.DashboardSemDadosException;


@Service
public class MetricaService {

    private final WebClient webClient;

    @Autowired
    public MetricaService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 3. Novo método para buscar dados via API (Gateway -> MS-Tarefas)
     * Usa .block() para fazer a chamada síncrona (como pedido pelo seu professor)
     */
    private List<TarefaDTO> buscarTarefasDoProjeto(String projId) {
        List<TarefaDTO> tarefas = webClient.get()
                .uri("/api/tasks-data/project/{projId}", projId)
                .retrieve()
                .bodyToFlux(TarefaDTO.class)
                .collectList()
                .block();

        if (tarefas == null || tarefas.isEmpty()) {
            throw new DashboardSemDadosException("Dados Indisponíveis",
                    "Não há tarefas cadastradas neste projeto para gerar métricas.");
        }
        return tarefas;
    }


    public List<Map<String, Object>> calcularPrazosPorMembro(String projId) {
        List<TarefaDTO> tarefas = buscarTarefasDoProjeto(projId);

        List<TarefaDTO> concluidas = tarefas.stream()
                .filter(t -> "Concluída".equalsIgnoreCase(t.getTarStatus()))
                .toList();

        if (concluidas.isEmpty())
            return List.of();

        Map<String, List<TarefaDTO>> tarefasPorMembro = concluidas.stream()
                .filter(t -> t.getResponsaveis() != null && !t.getResponsaveis().isEmpty())
                .flatMap(tarefa -> tarefa.getResponsaveis().stream()
                        .map(resp -> Map.entry(resp.getUsuNome(), tarefa)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Map.Entry<String, List<TarefaDTO>> entry : tarefasPorMembro.entrySet()) {
            String usuario = entry.getKey();
            List<TarefaDTO> tarefasUsuario = entry.getValue();

            long dentroPrazo = tarefasUsuario.stream()
                    .filter(t -> Boolean.TRUE.equals(t.getConcluidaNoPrazo()))
                    .count();

            long foraPrazo = tarefasUsuario.stream()
                    .filter(t -> Boolean.FALSE.equals(t.getConcluidaNoPrazo()))
                    .count();

            Map<String, Object> dados = new HashMap<>();
            dados.put("usuNome", usuario);
            dados.put("dentroPrazo", dentroPrazo);
            dados.put("foraPrazo", foraPrazo);

            resultado.add(dados);
        }

        return resultado;
    }

    public Map<String, Long> calcularPrazosGerais(String projId) {
        List<TarefaDTO> tarefas = buscarTarefasDoProjeto(projId);

        List<TarefaDTO> concluidas = tarefas.stream()
                .filter(t -> "Concluída".equalsIgnoreCase(t.getTarStatus()))
                .toList();

        if (concluidas.isEmpty()) {
            return Map.of("dentroPrazo", 0L, "foraPrazo", 0L);
        }

        long dentroPrazo = concluidas.stream()
                .filter(t -> Boolean.TRUE.equals(t.getConcluidaNoPrazo()))
                .count();

        long foraPrazo = concluidas.stream()
                .filter(t -> Boolean.FALSE.equals(t.getConcluidaNoPrazo()))
                .count();

        return Map.of(
                "dentroPrazo", dentroPrazo,
                "foraPrazo", foraPrazo);
    }

    public List<Map<String, Object>> contarTarefasConcluidasPorMembro(String projId) {
        List<TarefaDTO> tarefas = buscarTarefasDoProjeto(projId);

        List<TarefaDTO> concluidas = tarefas.stream()
                .filter(t -> "Concluída".equalsIgnoreCase(t.getTarStatus()))
                .toList();

        Map<String, Map<String, Object>> agrupadas = new HashMap<>();

        for (TarefaDTO tarefa : concluidas) {
            if (tarefa.getResponsaveis() == null || tarefa.getResponsaveis().isEmpty())
                continue;

            for (var resp : tarefa.getResponsaveis()) {
                String nome = resp.getUsuNome();
                if (nome == null || nome.isBlank())
                    continue;

                agrupadas.putIfAbsent(nome, new HashMap<>());
                Map<String, Object> dados = agrupadas.get(nome);

                Long count = (Long) dados.getOrDefault("tarefasConcluidas", 0L);
                dados.put("tarefasConcluidas", count + 1);

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> datasConclusao = (List<Map<String, Object>>) dados
                        .getOrDefault("datasConclusao",
                                new ArrayList<Map<String, Object>>());

                if (tarefa.getTarDataConclusao() != null) {
                    Map<String, Object> dataInfo = new HashMap<>();
                    dataInfo.put("data", tarefa.getTarDataConclusao());
                    dataInfo.put("noPrazo", Boolean.TRUE.equals(tarefa.getConcluidaNoPrazo()));
                    dataInfo.put("id", tarefa.getTarId());
                    datasConclusao.add(dataInfo);
                }
                dados.put("datasConclusao", datasConclusao);
                dados.put("usuNome", nome);
                dados.put("tarId", tarefa.getTarId());
            }
        }
        return new ArrayList<>(agrupadas.values());
    }

    public Map<String, Object> calcularProdutividadePorMembro(String projId) {
        List<TarefaDTO> tarefas = buscarTarefasDoProjeto(projId);

        Map<YearMonth, Map<String, List<TarefaDTO>>> dadosAgrupados = new TreeMap<>();

        for (TarefaDTO tarefa : tarefas) {
            if (!"Concluída".equalsIgnoreCase(tarefa.getTarStatus())
                    || tarefa.getTarDataConclusao() == null
                    || tarefa.getResponsaveis() == null
                    || tarefa.getResponsaveis().isEmpty()) {
                continue;
            }

            try {
                LocalDate dataConclusao = LocalDate.parse(tarefa.getTarDataConclusao());

                YearMonth mes = YearMonth.from(dataConclusao);

                for (var resp : tarefa.getResponsaveis()) {
                    String nome = resp.getUsuNome();
                    if (nome == null || nome.isBlank())
                        continue;

                    dadosAgrupados.putIfAbsent(mes, new HashMap<>());
                    dadosAgrupados.get(mes).putIfAbsent(nome, new ArrayList<>());
                    dadosAgrupados.get(mes).get(nome).add(tarefa);
                }

            } catch (DateTimeParseException e) {
            }
        }

        List<Map.Entry<YearMonth, Map<String, List<TarefaDTO>>>> allEntries = new ArrayList<>(
                dadosAgrupados.entrySet());

        int startIndex = Math.max(0, allEntries.size() - 6);

        List<Map.Entry<YearMonth, Map<String, List<TarefaDTO>>>> lastSixEntries = allEntries
                .subList(startIndex, allEntries.size());

        Set<String> usersInLastSix = new HashSet<>();
        for (Map.Entry<YearMonth, Map<String, List<TarefaDTO>>> entry : lastSixEntries) {
            usersInLastSix.addAll(entry.getValue().keySet());
        }

        List<Map<String, Object>> resultadoFormatado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yy");

        for (Map.Entry<YearMonth, Map<String, List<TarefaDTO>>> entry : lastSixEntries) {
            YearMonth mes = entry.getKey();
            Map<String, List<TarefaDTO>> dadosDoMes = entry.getValue();

            Map<String, Object> linhaMes = new HashMap<>();
            linhaMes.put("mes", mes.format(formatter));

            for (String usuario : usersInLastSix) {
                List<TarefaDTO> tarefasUsuarioMes = dadosDoMes.getOrDefault(usuario, List.of());

                long total = tarefasUsuarioMes.size();
                long noPrazo = tarefasUsuarioMes.stream()
                        .filter(t -> Boolean.TRUE.equals(t.getConcluidaNoPrazo()))
                        .count();

                double produtividade = (total > 0) ? (noPrazo * 100.0) / total : 0.0;
                linhaMes.put(usuario, Math.round(produtividade * 10.0) / 10.0);
            }
            resultadoFormatado.add(linhaMes);
        }

        Map<String, Object> resultadoFinal = new HashMap<>();
        resultadoFinal.put("usuarios", usersInLastSix);
        resultadoFinal.put("dadosMensais", resultadoFormatado);

        return resultadoFinal;
    }
}