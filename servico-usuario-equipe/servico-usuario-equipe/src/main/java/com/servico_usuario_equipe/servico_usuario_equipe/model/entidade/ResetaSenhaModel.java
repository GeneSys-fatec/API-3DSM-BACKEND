package com.servico_usuario_equipe.servico_usuario_equipe.model.entidade;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "ResetToken")
public class ResetaSenhaModel {
    @Id
    private String id;

    private String token;
    private String email; 
    
    @Indexed(expireAfterSeconds = 1800) 
    private LocalDateTime dataCriacao;

    public ResetaSenhaModel(String token, String email) {
        this.token = token;
        this.email = email;
        this.dataCriacao = LocalDateTime.now();
    }
    
    public ResetaSenhaModel() {
    }
}
