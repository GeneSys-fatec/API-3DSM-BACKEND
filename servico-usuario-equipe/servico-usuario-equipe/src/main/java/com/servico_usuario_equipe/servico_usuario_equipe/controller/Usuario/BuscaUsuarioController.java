package com.servico_usuario_equipe.servico_usuario_equipe.controller.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servico_usuario_equipe.servico_usuario_equipe.model.AdicionadorLinkUsuario;
import com.servico_usuario_equipe.servico_usuario_equipe.model.converter.UsuarioConverter;
import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.usuario.UsuarioDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Usuario.BuscaUsuarioService;

@RestController
@RequestMapping("/usuario")
public class BuscaUsuarioController {
    @Autowired
    private BuscaUsuarioService buscaUsuarioService;
    @Autowired
    private AdicionadorLinkUsuario adicionadorLink;
    @Autowired
    private UsuarioConverter usuarioConverterService;

    @GetMapping("/{usuId}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable String usuId) {
        Optional<UsuarioModel> usuarioOpt = buscaUsuarioService.buscarPorId(usuId);
        if (usuarioOpt.isPresent()) {
            UsuarioDTO dto = usuarioConverterService.modelParaDto(usuarioOpt.get());
            adicionadorLink.adicionarLink(dto);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    } 

    @GetMapping("/buscar/{usuEmail}")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable String usuEmail) {
        Optional<UsuarioModel> usuarioOpt = buscaUsuarioService.buscarPorEmail(usuEmail);
        if (usuarioOpt.isPresent()) {
            UsuarioDTO dto = usuarioConverterService.modelParaDto(usuarioOpt.get());
            adicionadorLink.adicionarLink(dto);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listarUsuario() {
        List<UsuarioModel> usuarios = buscaUsuarioService.listarTodas();
        List<UsuarioDTO> dtos = usuarios.stream()
            .map(usuarioConverterService::modelParaDto)
            .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

}
