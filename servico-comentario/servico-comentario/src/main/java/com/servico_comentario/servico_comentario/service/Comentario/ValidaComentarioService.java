package com.servico_comentario.servico_comentario.service.Comentario;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.servico_comentario.servico_comentario.exceptions.personalizados.comentário.ComentarioEmBrancoException;
import com.servico_comentario.servico_comentario.exceptions.personalizados.comentário.ConteudoInapropriadoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//import com.servico_comentario.servico_comentario.exceptions.personalizados.comentário.ComentarioEmBrancoException;
//import com.servico_comentario.servico_comentario.exceptions.personalizados.comentário.ConteudoInapropriadoException;
import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;

import jakarta.annotation.PostConstruct;


@Service
public class ValidaComentarioService {

    @Value("${app.validation.palavras-proibidas:palavrao1,ofensa,improprio}")
    private String palavrasProibidasConfig;

    private Set<String> PALAVRAS_PROIBIDAS;

    @PostConstruct
    public void init() {
        PALAVRAS_PROIBIDAS = new HashSet<>(Arrays.asList(palavrasProibidasConfig.split(",")));
    }

    public void validarConteudo(String mensagem) {
        String mensagemNormalizada = mensagem.toLowerCase();

        for (String palavra : PALAVRAS_PROIBIDAS) {
            if (mensagemNormalizada.contains(palavra)) {
                throw new ConteudoInapropriadoException("Mensagem do comentário contém palavra imprópria","O comentário contém palavras impróprias e não pode ser salvo.");
            }
        }
    }

    public void validarComentario(ComentarioModel comentario) {
        String mensagem = comentario.getComMensagem();

        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new ComentarioEmBrancoException("Mensagem do comentário em branco", "A mensagem não pode estar em branco!");
        }
        validarConteudo(mensagem);
    }
}