package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.segurancaFiltro;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; // NOVO IMPORT
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.UsuarioDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Auth.CookieService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Token.ValidaTokenService;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.utils.CryptoUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    ValidaTokenService validaTokenService;

    // --- INÍCIO DA CORREÇÃO ---
    @Autowired
    @Qualifier("usuarioWebClient") // Diz ao Spring para usar o bean com o nome "usuarioWebClient"
            WebClient userWebClient;
    // --- FIM DA CORREÇÃO ---

    @Autowired
    CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // O seu 'if' para ignorar a API interna
        String path = request.getServletPath();
        if (path.startsWith("/api/tasks-data/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        if (token != null && !token.isEmpty()) {
            try {
                String decryptedToken = CryptoUtils.decrypt(token, cookieService.getSecret());
                System.out.println("Decrypted Token: " + decryptedToken);

                var login = validaTokenService.validateToken(decryptedToken);

                // Esta chamada agora usa o WebClient correto
                UsuarioDTO usuario = userWebClient.get()
                        .uri("/auth/session")
                        .cookie("jwt-token", token)
                        .retrieve()
                        .bodyToMono(UsuarioDTO.class)
                        .block();

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