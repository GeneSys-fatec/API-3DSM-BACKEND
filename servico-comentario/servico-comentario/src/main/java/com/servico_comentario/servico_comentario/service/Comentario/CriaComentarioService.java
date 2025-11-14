package com.servico_comentario.servico_comentario.service.Comentario;

import com.servico_comentario.servico_comentario.model.dto.UsuarioPrincipalDTO;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;
// import reactor.core.publisher.Mono;

import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;
import com.servico_comentario.servico_comentario.repository.ComentarioRepository;


// record TarefaResponseDTO(String tarId, java.util.List<ResponsavelTarefa> responsaveis) {}
// record ResponsavelTarefa(String usuId) {}
// record NotificacaoRequestDTO(String deUsuId, String paraUsuId, String tipo, String referenciaId) {}


@Service
public class CriaComentarioService {

    @Autowired
    private ComentarioRepository repository;

    @Autowired
    private ValidaComentarioService validaComentarioService;

    // @Autowired
    // @Qualifier("projetoWebClient")
    // private WebClient projetoWebClient;

    // @Autowired
    // @Qualifier("notificacaoWebClient")
    // private WebClient notificacaoWebClient;


    public ComentarioModel adicionarComentario(ComentarioModel comentario, UsuarioPrincipalDTO principal) {
        validaComentarioService.validarComentario(comentario);

        ComentarioModel comentarioSalvo = repository.save(comentario);

        // dispararNotificacoes(comentarioSalvo, principal);

        return comentarioSalvo;
    }


    /*
    private void dispararNotificacoes(ComentarioModel comentario, UsuarioPrincipalDTO principal) {

        projetoWebClient.get()
                .uri("/tarefa/interno/" + comentario.getTarId())
                .retrieve()
                .bodyToMono(TarefaResponseDTO.class)
                .flatMap(tarefa -> {
                    if (tarefa == null || tarefa.responsaveis() == null) {
                        return Mono.empty();
                    }

                    for (ResponsavelTarefa responsavel : tarefa.responsaveis()) {

                        if (!responsavel.usuId().equals(principal.id())) {

                            NotificacaoRequestDTO notificacao = new NotificacaoRequestDTO(
                                    principal.id(),
                                    responsavel.usuId(),
                                    "NOVO_COMENTARIO",
                                    comentario.getTarId()
                            );

                            notificacaoWebClient.post()
                                    .uri("/notificacao/criar")
                                    .bodyValue(notificacao)
                                    .retrieve()
                                    .bodyToMono(Void.class)
                                    .subscribe();
                        }
                    }
                    return Mono.empty();
                })
                .doOnError(e -> System.err.println("Falha ao buscar tarefa/notificar: " + e.getMessage()))
                .subscribe();
    }
    */
}