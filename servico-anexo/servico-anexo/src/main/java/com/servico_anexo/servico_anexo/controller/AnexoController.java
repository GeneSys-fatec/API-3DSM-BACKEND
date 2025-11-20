package com.servico_anexo.servico_anexo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.servico_anexo.servico_anexo.service.FileStorageService;

@RestController
@RequestMapping("/anexos")
public class AnexoController {

    @Autowired
    private FileStorageService storage;

    private boolean badId(String id) {
        return id == null || id.isBlank() || "undefined".equalsIgnoreCase(id) || "null".equalsIgnoreCase(id);
    }

    @PostMapping("/tarefa/{id}/upload")
    public ResponseEntity<?> upload(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        try {
            if (badId(id)) return ResponseEntity.badRequest().body("ID da tarefa inválido");
            String saved = storage.save(id, file);
            // Retorna um objeto JSON no formato {"url": "nome_do_arquivo.ext"}
            return ResponseEntity.ok(Map.of("url", saved));
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().body("Erro ao salvar: " + ex.getMessage());
        }
    }

    @GetMapping("/tarefa/{id}")
    public ResponseEntity<?> listar(@PathVariable String id) {
        try {
            if (badId(id)) return ResponseEntity.badRequest().body("ID da tarefa inválido");
            
            // Mapeia a lista de strings para uma lista de objetos
            List<Map<String, String>> anexosComoObjeto = storage.list(id).stream()
                .map(nomeArquivo -> Map.of("arquivoNome", nomeArquivo))
                .collect(Collectors.toList());

            return ResponseEntity.ok(anexosComoObjeto);
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().body("Erro ao listar: " + ex.getMessage());
        }
    }

    @GetMapping("/{idTarefa}/{nomeArquivo}")
    public ResponseEntity<?> baixar(@PathVariable String idTarefa, @PathVariable String nomeArquivo) {
        try {
            if (badId(idTarefa)) return ResponseEntity.badRequest().body("ID da tarefa inválido");
            Resource resource = storage.load(idTarefa, nomeArquivo);
            String contentType = null;
            try {
                Path p = storage.taskFolder(idTarefa).resolve(nomeArquivo);
                contentType = Files.probeContentType(p);
            } catch (IOException ignore) {}
            if (contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().body("Erro ao carregar: " + ex.getMessage());
        }
    }
}
