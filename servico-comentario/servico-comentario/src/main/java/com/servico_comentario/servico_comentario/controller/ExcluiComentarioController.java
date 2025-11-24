package com.servico_comentario.servico_comentario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_comentario.servico_comentario.service.Comentario.ExcluiComentarioService;

@RestController
@RequestMapping("/comentario")
public class ExcluiComentarioController {

    @Autowired
    private ExcluiComentarioService excluiComentarioService;

    @DeleteMapping("/apagar/{comId}")
    public ResponseEntity<String> apagarComentario(@PathVariable String comId) {

        excluiComentarioService.excluirComentario(comId);

        return ResponseEntity.ok("Comentário apagado com sucesso!");
    }
}
