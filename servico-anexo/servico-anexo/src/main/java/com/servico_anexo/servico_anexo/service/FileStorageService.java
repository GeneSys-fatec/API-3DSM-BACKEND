package com.servico_anexo.servico_anexo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    private String sanitize(String name) {
        String n = name.replace("\\", "/");
        n = n.substring(n.lastIndexOf('/') + 1);
        return n.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public Path taskFolder(String taskId) {
        return root.resolve(taskId);
    }

    public String save(String taskId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Arquivo vazio");
        }
        String filename = sanitize(file.getOriginalFilename() != null ? file.getOriginalFilename() : "arquivo");
        Path folder = taskFolder(taskId);
        Files.createDirectories(folder);
        Path target = folder.resolve(filename);
        Files.write(target, file.getBytes());
        return filename;
    }

    public List<String> list(String taskId) throws IOException {
        Path folder = taskFolder(taskId);
        if (!Files.exists(folder)) return List.of();
        try (var stream = Files.list(folder)) {
            return stream.filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    public Resource load(String taskId, String filename) throws IOException {
        Path file = taskFolder(taskId).resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("Arquivo não encontrado: " + filename);
        }
        return resource;
    }
}
