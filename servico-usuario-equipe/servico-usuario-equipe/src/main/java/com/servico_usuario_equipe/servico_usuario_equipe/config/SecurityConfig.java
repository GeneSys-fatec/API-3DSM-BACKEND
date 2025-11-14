package com.servico_usuario_equipe.servico_usuario_equipe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.servico_usuario_equipe.servico_usuario_equipe.segurancaFiltro.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain rotas(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST,"/auth/cadastrar").permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/session").permitAll() 
                .requestMatchers(HttpMethod.GET, "/usuario/buscar/**").permitAll() 
                .requestMatchers(HttpMethod.GET, "/equipe/interno/validar-membro").permitAll()
                .requestMatchers(HttpMethod.GET, "/equipe/buscar-ids-por-usuario/**").permitAll()
                .requestMatchers(
                        "/boasvindas",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                 ).permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}