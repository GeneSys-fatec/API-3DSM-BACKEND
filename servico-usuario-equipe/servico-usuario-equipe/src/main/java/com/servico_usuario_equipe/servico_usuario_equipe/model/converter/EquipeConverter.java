package com.servico_usuario_equipe.servico_usuario_equipe.model.converter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // <-- IMPORTAR
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component; // <-- IMPORTAR
import org.springframework.web.reactive.function.client.WebClient;

import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.EquipeDTO; // <-- IMPORTAR
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.ProjetoDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.usuario.UsuarioDTO; // O "espelho" DTO
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.EquipeModel;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;

@Component
public class EquipeConverter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // 1. TROQUE o ProjetoConverter (que não existe aqui)
    // @Autowired
    // private ProjetoConverter projetoConverter;

    // 2. ADICIONE o WebClient (que você criou no AppConfig)
    @Autowired
    private WebClient projetoWebClient;

    public EquipeDTO modelParaDto(EquipeModel model) {
        if (model == null) {
            return null;
        }

        EquipeDTO dto = new EquipeDTO();
        
        // --- 3. SEUS MAPEAMENTOS SIMPLES (NENHUMA MUDANÇA) ---
        dto.setEquId(model.getEquId());
        dto.setEquNome(model.getEquNome());
        dto.setEquDescricao(model.getEquDescricao());

        if (model.getEquDataCriacao() != null) {
            dto.setEquDataCriacao(model.getEquDataCriacao().format(formatter));
        }
        if (model.getEquDataAtualizacao() != null) {
            dto.setEquDataAtualizacao(model.getEquDataAtualizacao().format(formatter));
        }

        // --- 4. MAPEAMENTO DE USUÁRIOS (NENHUMA MUDANÇA) ---
        if (model.getUsuarios() != null) {
            dto.setEquMembros(model.getUsuarios().stream()
                    .map(this::usuarioModelParaDto)
                    .collect(Collectors.toList()));
        }

        // --- 5. LÓGICA DE AGREGAÇÃO DE PROJETOS (A GRANDE MUDANÇA) ---
        
        List<ProjetoDTO> projetosDetalhados = new ArrayList<>();
        
        // 5a. Use "getProjetoIds()" (a lista de Strings)
        if (model.getProjetosIds() != null && !model.getProjetosIds().isEmpty()) {
            
            String idsParaUrl = String.join(",", model.getProjetosIds());

            try {
                // 5b. Use o WebClient para buscar os projetos
                projetosDetalhados = projetoWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                        .path("/projetos/batch") // Endpoint no projeto-service
                        .queryParam("ids", idsParaUrl) // Parâmetro da URL
                        .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ProjetoDTO>>() {})
                    .block(); // Espera a resposta

            } catch (Exception e) {
                System.err.println("Erro ao buscar projetos para equipe " + model.getEquId() + ": " + e.getMessage());
                // (Retorna a lista vazia em caso de erro)
            }
        }
        
        // 5c. Defina a lista de projetos (cheia ou vazia) no DTO
        dto.setProjetos(projetosDetalhados);

        return dto;
    }

    // --- (O resto da classe não muda) ---

    public EquipeModel dtoParaModel(EquipeDTO dto) {
        if (dto == null) {
            return null;
        }
        EquipeModel model = new EquipeModel();
        model.setEquNome(dto.getEquNome());
        model.setEquDescricao(dto.getEquDescricao());
        return model;
    }

    private UsuarioDTO usuarioModelParaDto(UsuarioModel model) {
        if (model == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsuId(model.getUsuId());
        dto.setUsuNome(model.getUsuNome());
        dto.setUsuEmail(model.getUsuEmail());
        dto.setUsuCaminhoFoto(model.getUsuCaminhoFoto());
        return dto;
    }
}