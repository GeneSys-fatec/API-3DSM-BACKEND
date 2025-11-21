package com.servico_auditoria.servico_auditoria.segurancaFiltro;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.servico_auditoria.servico_auditoria.client.UsuarioClient;
import com.servico_auditoria.servico_auditoria.model.dto.UsuarioDTO;
import com.servico_auditoria.servico_auditoria.service.Auth.CookieService;
import com.servico_auditoria.servico_auditoria.service.Token.ValidaTokenService;

import java.io.IOException;
import java.util.Collections;


@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    ValidaTokenService validaTokenService;

    // --- INÍCIO DA CORREÇÃO ---
    @Autowired
    UsuarioClient usuarioClient;

    @Autowired
    CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        if (path.startsWith("/auditoria/") || path.startsWith("/actuator/")) {
            chain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);
        if (token != null && !token.isEmpty()) {
            try {
                UsuarioDTO usuario = usuarioClient.getUsuarioSessao(token);

                if (usuario == null) {
                    throw new RuntimeException("Usuário não encontrado no user-service");
                }
                
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Não autorizado: " + ex.getMessage());
                return;
            }
            chain.doFilter(request, response);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
