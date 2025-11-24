package com.servico_usuario_equipe.servico_usuario_equipe.segurancaFiltro;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.UsuarioRepository;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Auth.CookieService;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Token.ValidaTokenService;
import com.servico_usuario_equipe.servico_usuario_equipe.utils.CryptoUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    ValidaTokenService validaTokenService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    CookieService cookieService;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/auth/cadastrar",
            "/equipe/buscar-ids-por-usuario/**",
            "/equipe/interno/validar-membro",
            "/boasvindas",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return PUBLIC_PATHS.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

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
                System.out.println("Decrypted Token: " + decryptedToken);

                var usuId = validaTokenService.validateToken(decryptedToken);

                UsuarioModel usuario = usuarioRepository.findById(usuId)
                        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado",
                                "O ID referenciado no token não existe na base."));

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

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}