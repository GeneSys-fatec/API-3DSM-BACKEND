package com.servico_usuario_equipe.servico_usuario_equipe.service.Usuario;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class FotoStorageService {

    private final Path root = Paths.get(new File("").getAbsolutePath()).resolve("uploads"); 

    public Path getFotoPath(String caminhoRelativoSalvo) {
        return root.resolve(caminhoRelativoSalvo);
    }

    public Resource load(String caminhoRelativoSalvo) throws IOException {
        Path file = getFotoPath(caminhoRelativoSalvo);
        Resource resource = new UrlResource(file.toUri());
        
        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("Arquivo não encontrado: " + caminhoRelativoSalvo);
        }
        return resource;
    }
}