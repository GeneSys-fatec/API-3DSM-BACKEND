package com.servico_notificacao.servico_notificacao.segurancaFiltro;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.servico_notificacao.servico_notificacao.client.UsuarioClient;
import com.servico_notificacao.servico_notificacao.model.dto.UsuarioDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioClient usuarioClient;

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.startsWith("/notificacao/criar/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        log.debug("SecurityFilter - Token (cookie) presente? {}", token != null);
        if (token != null && !token.isBlank()) {
            try {



                UsuarioDTO usuario = usuarioClient.getUsuarioSessao(token);

                log.debug("SecurityFilter - Usuario resolvido? {}", (usuario != null ? usuario.getUsuId() : null));
                if (usuario != null && usuario.getUsuId() != null) {
                    var auth = new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ex) {
                log.debug("SecurityFilter - falha ao resolver usuario: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
    
    private String recoverToken(HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if ("jwt-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}