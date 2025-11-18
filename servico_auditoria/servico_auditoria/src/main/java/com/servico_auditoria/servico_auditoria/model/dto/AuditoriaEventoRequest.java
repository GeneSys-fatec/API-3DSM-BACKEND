package main.java.com.servico_auditoria.servico_auditoria.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaEventoRequest {

    @NotBlank
    private String projetoId;

    private String tarefaId;

    @NotBlank
    private String usuario;

    @NotBlank
    private String acao;

    private String traceId;
}
