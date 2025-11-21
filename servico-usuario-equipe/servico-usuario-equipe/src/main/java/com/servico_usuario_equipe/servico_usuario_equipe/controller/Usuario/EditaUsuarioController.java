package com.servico_usuario_equipe.servico_usuario_equipe.controller.Usuario;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.usuario.UsuarioDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Usuario.EditaUsuarioService;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Usuario.FotoStorageService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/usuario")
public class EditaUsuarioController {

    @Autowired
    private EditaUsuarioService editaUsuarioService;
    @Autowired
    private FotoStorageService fotoStorageService;

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarNomeEmail(
        @PathVariable String id, @RequestBody UsuarioDTO dto, HttpServletResponse response) { 
        var atualizado = editaUsuarioService.atualizarNomeEmail(id, dto.getUsuNome(), dto.getUsuEmail(), response);
        return ResponseEntity.ok(atualizado);
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<?> atualizarSenha(@PathVariable String id, @RequestBody Map<String, String> body) {
        editaUsuarioService.atualizarSenha(id, body.get("senhaAtual"), body.get("novaSenha"));
        return ResponseEntity.ok("Senha atualizada com sucesso."); 
    }

    @PostMapping("/{id}/foto") 
    public ResponseEntity<UsuarioModel> atualizarFoto(
        @PathVariable String id, @RequestParam("foto") MultipartFile arquivo) {
        try {
            UsuarioModel usuarioAtualizado = editaUsuarioService.atualizarFoto(id, arquivo);
            return ResponseEntity.ok(usuarioAtualizado); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }

    @GetMapping("/foto/{caminhoRelativoSalvo}")
    public ResponseEntity<Resource> buscarFoto(@PathVariable String caminhoRelativoSalvo) {
        try {
            Resource resource = fotoStorageService.load("fotos/" + caminhoRelativoSalvo);
            String contentType = Files.probeContentType(resource.getFile().toPath());
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
