package com.servico_anexo.servico_anexo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableMongoAuditing
public class ServicoAnexoApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
    	dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(ServicoAnexoApplication.class, args);
	}

}
