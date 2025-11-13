package com.servico_notificacao.servico_notificacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.servico_notificacao.servico_notificacao.segurancaFiltro.SecurityFilter;

@Configuration
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(b -> b.disable())
            .formLogin(f -> f.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex.authenticationEntryPoint((req,res,e) -> {
                // Retorna 401 sempre que não autenticado, evitando 403 confuso.
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Não autenticado");
            }))
            .authorizeHttpRequests(auth -> auth
                // Temporariamente libera listar para validar frontend sem token.
                .requestMatchers("/notificacao/listar").permitAll()
                // Exige auth para alterações.
                .requestMatchers("/notificacao/marcar-todas", "/notificacao/marcar-lida/**").authenticated()
                // Swagger e utilidades
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()
                .anyRequest().permitAll())
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
