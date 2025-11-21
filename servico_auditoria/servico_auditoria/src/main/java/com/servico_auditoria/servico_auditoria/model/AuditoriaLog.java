package com.servico_auditoria.servico_auditoria.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import com.servico_auditoria.servico_auditoria.model.dto.ModificacaoLogDto;
import com.servico_auditoria.servico_auditoria.model.dto.ResponsavelAlteracaoDto;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "auditoria_logs")
@Data
@Getter
@Setter
public class AuditoriaLog {

    private String id;

    private LocalDateTime criadoEm;

    private String projetoId;

    private String tarefaId;

    private String traceId;

    private String tarefaNome;

    private ResponsavelAlteracaoDto responsavel;

    private List<ModificacaoLogDto> modificacoes;
}
