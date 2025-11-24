package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.segurancaFiltro;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalApiAuthFilter extends OncePerRequestFilter {

    @Value("${internal.api.key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // --- LOG 1 ---
        System.out.println("[InternalApiAuthFilter] Path: " + path);

        if (!path.startsWith("/api/tasks-data/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // --- LOG 2 ---
        String key = request.getHeader("X-Internal-API-Key");
        System.out.println("[InternalApiAuthFilter] Chave recebida: " + key);

        if (key != null && key.equals(apiKey)) {
            // --- LOG 3 ---
            System.out.println("[InternalApiAuthFilter] Chave VÁLIDA. A permitir passagem.");
            filterChain.doFilter(request, response);
        } else {
            // --- LOG 4 ---
            System.out.println("[InternalApiAuthFilter] Chave INVÁLIDA ou AUSENTE. A bloquear com 403.");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chave de API interna inválida ou ausente.");
        }
    }
}