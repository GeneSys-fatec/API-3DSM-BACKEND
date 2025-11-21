package com.servico_comentario.servico_comentario.controller;

import java.util.Date;

import com.servico_comentario.servico_comentario.model.AdicionadorLinkComentario;
import com.servico_comentario.servico_comentario.model.dto.UsuarioPrincipalDTO;
import com.servico_comentario.servico_comentario.service.Comentario.EditaComentarioService;
import com.servico_comentario.servico_comentario.service.Comentario.ValidaComentarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import com.servico_comentario.servico_comentario.model.converter.ComentarioConverter;
import com.servico_comentario.servico_comentario.model.dto.ComentarioDTO;
import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;
import com.servico_comentario.servico_comentario.service.Comentario.BuscaComentarioService;

@RestController
@RequestMapping("/comentario")
public class EditaComentarioController {

    @Autowired
    private BuscaComentarioService buscaComentarioService;

    @Autowired
    private EditaComentarioService editaComentarioService;

    @Autowired
    private AdicionadorLinkComentario adicionadorLink;

    @Autowired
    private ComentarioConverter comentarioConverterService;

    @PutMapping("/atualizar/{comId}")
    public ResponseEntity<ComentarioDTO> atualizarComentario(
            @PathVariable String comId,
            @RequestBody ComentarioDTO dto) {


        ComentarioModel existente = buscaComentarioService
                .listarPorId(comId)
                .orElse(null);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setComMensagem(dto.getComMensagem());
        existente.setComDataAtualizacao(new Date());

        ComentarioModel atualizado = editaComentarioService.atualizarComentario(existente);

        ComentarioDTO dtoAtualizado = comentarioConverterService.modelParaDto(atualizado);
        adicionadorLink.adicionarLink(dtoAtualizado);

        return ResponseEntity.ok(dtoAtualizado);
    }
}
