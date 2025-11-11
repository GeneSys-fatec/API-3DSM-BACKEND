package com.servico_comentario.servico_comentario.segurancaFiltro;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.servico_comentario.servico_comentario.service.Auth.CookieService;
import com.servico_comentario.servico_comentario.service.Token.ValidaTokenService;
import com.servico_comentario.servico_comentario.utils.CryptoUtils;
import com.servico_comentario.servico_comentario.model.dto.UsuarioPrincipalDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    ValidaTokenService validaTokenService;

    @Autowired
    CookieService cookieService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        if (token != null && !token.isEmpty()) {
            try {
                String decryptedToken = CryptoUtils.decrypt(token, cookieService.getSecret());

                var login = validaTokenService.validateToken(decryptedToken);
                if (login == null || login.isEmpty()) {
                    throw new RuntimeException("Token inválido ou expirado.");
                }

                String usuId = validaTokenService.getClaim(decryptedToken, "usuId");
                String usuNome = validaTokenService.getClaim(decryptedToken, "usuNome");

                if (usuId == null || usuNome == null) {
                    throw new RuntimeException("Token não contém claims essenciais (usuId, usuNome).");
                }

                UsuarioPrincipalDTO principal = new UsuarioPrincipalDTO(usuId, usuNome);

                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

                var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                response.getWriter().write("Não autorizado: Acesso negado. " + ex.getMessage());

                return;
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