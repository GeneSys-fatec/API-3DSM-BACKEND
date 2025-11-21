package com.servico_auditoria.servico_auditoria.model.dto;

public record AuditoriaResponseDto(
        String projetoId,
        String tarefaId,
        String tarefaNome,
        ResponsavelAlteracaoDto responsavel,
        ModificacaoLogDto modificacao,
        String dataAlteracao,
        String horaAlteracao,
        String traceId
) {}
