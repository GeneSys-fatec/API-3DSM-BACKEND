package com.servico_comentario.servico_comentario.service.Comentario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;
import com.servico_comentario.servico_comentario.repository.ComentarioRepository;

@Service
public class ExcluiComentarioService {

    @Autowired
    private ComentarioRepository repository;

    @Autowired
    private ValidaComentarioService validaComentarioService;

    @Autowired
    private BuscaComentarioService buscaComentarioService;

    public void excluirComentario(String comId) {

        ComentarioModel comentario = buscaComentarioService.listarPorId(comId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado."));

        validaComentarioService.verificarSeUsuarioPodeExcluirComentario(comentario);

        excluirRespostas(comentario.getComId());

        repository.deleteById(comentario.getComId());
    }

    private void excluirRespostas(String comId) {
        List<ComentarioModel> respostas = repository.findByRespostaComentario(comId);

        if (respostas != null && !respostas.isEmpty()) {
            for (ComentarioModel resposta : respostas) {
                excluirRespostas(resposta.getComId());
                repository.deleteById(resposta.getComId());
            }
        }
    }
}
