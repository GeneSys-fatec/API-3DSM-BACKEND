package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.converter.ProjetoConverter;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ProjetoDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ProjetoRepository;

@Service
public class BuscaProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private ProjetoConverter projetoConverter;

    @Autowired
    private WebClient userWebClient;

    public List<ProjetoModel> listarPorUsuario(String usuId) {

        // 2. BUSCAR AS EQUIPES DO USUÁRIO NO SERVIÇO 8083
        List<String> listaDeEquipes;
        try {
            // Esta rota precisa existir no seu serviço 8083
            // Ex: GET http://localhost:8083/equipes/buscar-ids-por-usuario/ID_DO_USUARIO
            listaDeEquipes = userWebClient.get()
                .uri("/equipe/buscar-ids-por-usuario/" + usuId) 
                .retrieve()
                .bodyToMono(List.class) // Espera uma List<String> de IDs de equipe
                .block();

        } catch (Exception e) {
            // Se falhar a comunicação, registre o erro e retorne uma lista vazia
            System.err.println("Erro ao buscar equipes do usuário: " + e.getMessage());
            return Collections.emptyList(); 
        }

        if (listaDeEquipes == null || listaDeEquipes.isEmpty()) {
            return Collections.emptyList(); // Usuário não está em nenhuma equipe
        }

        // 3. BUSCAR NO REPOSITÓRIO LOCAL USANDO O NOVO MÉTODO
        return projetoRepository.findAllByEquIdIn(listaDeEquipes);
    }

    public List<ProjetoModel> listarTodas() {
        return projetoRepository.findAll();
    }

    public Optional<ProjetoModel> buscarPorId(String id) {
        return projetoRepository.findById(id);
    }

    public List<ProjetoDTO> buscarPorListaDeIds(List<String> ids) {
        List<ProjetoModel> projetos = projetoRepository.findAllByProjIdIn(ids);
        return projetos.stream()
                .map(projetoConverter::modelParaDto)
                .collect(Collectors.toList());
    }
}