package com.servico_comentario.servico_comentario.controller;

import com.servico_comentario.servico_comentario.model.dto.UsuarioPrincipalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import com.servico_comentario.servico_comentario.service.Comentario.ExcluiComentarioService;
import com.servico_comentario.servico_comentario.service.Comentario.BuscaComentarioService;
import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;

@RestController
@RequestMapping("/comentario")
public class ExcluiComentarioController {

    @Autowired
    private ExcluiComentarioService excluiComentarioService;

    @Autowired
    private BuscaComentarioService buscaComentarioService;

    @DeleteMapping("/apagar/{comId}")
    public ResponseEntity<String> apagarComentario(@PathVariable String comId) {

        ComentarioModel existente = buscaComentarioService.listarPorId(comId).orElse(null);
        if (existente == null) {
            return ResponseEntity.ok("Comentário não encontrado, mas considerado apagado.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioPrincipalDTO principal = (UsuarioPrincipalDTO) authentication.getPrincipal();

        if (!existente.getUsuId().equals(principal.id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        excluiComentarioService.deletarRespostaComentario(comId);
        return ResponseEntity.ok("Comentário apagado com sucesso!");
    }
}