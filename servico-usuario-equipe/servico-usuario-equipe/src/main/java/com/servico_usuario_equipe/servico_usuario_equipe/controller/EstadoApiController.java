package com.servico_usuario_equipe.servico_usuario_equipe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EstadoApiController {
    @GetMapping("/boasvindas")
	public String boasVindas() {
		return "API funcionando!";
	}
}
