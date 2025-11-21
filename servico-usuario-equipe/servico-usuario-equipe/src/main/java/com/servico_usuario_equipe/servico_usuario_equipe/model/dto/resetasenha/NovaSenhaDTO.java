package com.servico_usuario_equipe.servico_usuario_equipe.model.dto.resetasenha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class NovaSenhaDTO {
    @NotBlank(message = "Senha é obrigatória!")
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$",
    message = "Senha fraca: deve ter mínimo 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais."
    )
    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
