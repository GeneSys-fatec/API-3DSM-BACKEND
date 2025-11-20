package com.servico_notificacao.servico_notificacao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.servico_notificacao.servico_notificacao.segurancaFiltro.SecurityFilter;
import com.servico_notificacao.servico_notificacao.segurancaFiltro.InternalApiAuthFilter;

@Configuration
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Autowired
    private InternalApiAuthFilter internalApiAuthFilter;

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
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Não autenticado");
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/notificacao/listar").authenticated()
                        .requestMatchers("/notificacao/marcar-todas", "/notificacao/marcar-lida/**").authenticated()

                        .requestMatchers("/notificacao/criar/**").permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(internalApiAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}