package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.config;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.segurancaFiltro.InternalApiAuthFilter;
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

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.segurancaFiltro.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private InternalApiAuthFilter internalApiAuthFilter;

    @Bean
    public SecurityFilterChain rotas(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/auth/cadastrar").permitAll()

                        .requestMatchers(HttpMethod.POST,"/usuario/buscar/{usuEmail}").permitAll()
                        .requestMatchers(
                                "/boasvindas",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tasks-data/**").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(internalApiAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}