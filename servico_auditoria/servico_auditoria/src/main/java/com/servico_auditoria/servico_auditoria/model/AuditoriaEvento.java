package main.java.com.servico_auditoria.servico_auditoria.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "auditoria_eventos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaEvento {

    @Id
    private String id;

    @Indexed
    private String projetoId;

    private String tarefaId;
    private String usuario;
    private String acao;
    private String traceId;
    private LocalDateTime data;
}
