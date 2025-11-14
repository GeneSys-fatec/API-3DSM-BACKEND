package com.servico_comentario.servico_comentario.controller;

import java.util.Date;

import com.servico_comentario.servico_comentario.model.AdicionadorLinkComentario;
import com.servico_comentario.servico_comentario.model.converter.ComentarioConverter;
import com.servico_comentario.servico_comentario.model.dto.ComentarioDTO;
import com.servico_comentario.servico_comentario.model.dto.UsuarioPrincipalDTO;
import com.servico_comentario.servico_comentario.model.entidade.ComentarioModel;
import com.servico_comentario.servico_comentario.service.Comentario.CriaComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;





@RestController
@RequestMapping("/comentario")
public class CriaComentarioController {
    @Autowired
    private CriaComentarioService criaComentarioService;
    @Autowired
    private AdicionadorLinkComentario adicionadorLink;
    @Autowired
    private ComentarioConverter comentarioConverterService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ComentarioDTO> cadastrarComentario(@RequestBody ComentarioDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioPrincipalDTO principal = (UsuarioPrincipalDTO) authentication.getPrincipal();

        ComentarioModel comentario = comentarioConverterService.dtoParaModel(dto);

        comentario.setUsuId(principal.id());
        comentario.setUsuNome(principal.nome());
        comentario.setComDataAtualizacao(new Date());

        ComentarioModel salvo = criaComentarioService.adicionarComentario(comentario, principal);

        ComentarioDTO dtoSalvo = comentarioConverterService.modelParaDto(salvo);
        adicionadorLink.adicionarLink(dtoSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvo);
    }
}