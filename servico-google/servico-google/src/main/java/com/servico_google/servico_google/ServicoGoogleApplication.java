package com.servico_google.servico_google;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableMongoAuditing
@ComponentScan(
    basePackages = "com.servico_google.servico_google",
    excludeFilters = {
        // Exclui qualquer bean que implemente UserDetailsService (ex.: customUserDetailsService)
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserDetailsService.class),
        // Exclui por nome de classe, caso exista
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*CustomUserDetailsService.*")
    }
)
public class ServicoGoogleApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(ServicoGoogleApplication.class, args);
    }
}
