package com.servico_auditoria.servico_auditoria.segurancaFiltro;

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

    @Value("${internal.api.key:}")
    private String internalApiKey;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // normalmente libere tudo e só usa a chave quando vier header
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Se vier a chave interna, apenas segue o fluxo; validação leve
        String key = request.getHeader("X-Internal-Api-Key");
        if (key != null && !key.isBlank() && internalApiKey != null && !internalApiKey.isBlank()) {
            // chave ok -> apenas continua (pode marcar atributo se precisar)
            request.setAttribute("internalApi", Boolean.TRUE);
        }
        filterChain.doFilter(request, response);
    }
}
