package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Coluna;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.coluna.NomeDeColunaJaExisteException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.dto.ColunaDTO;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ColunaModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ColunaRepository;

@Service
public class EditaColunaService {
    @Autowired
    private ColunaRepository colunaRepository;

    private static final Logger logger = LoggerFactory.getLogger(EditaColunaService.class);

    public ColunaDTO atualizarColuna(String col_id, ColunaDTO colunaDTO) {
        ColunaModel colunaExistente = colunaRepository.findById(col_id)
                .orElseThrow(() -> new ProjetoNaoEncontradoException("Coluna não encontrada",
                        "Coluna com id " + col_id + " não foi encontrada"));

        String novoTitulo = colunaDTO.getTitulo() == null ? "" : colunaDTO.getTitulo().trim();
        if (!novoTitulo.isEmpty() && !novoTitulo.equals(colunaExistente.getColTitulo())) {
            long duplicatas = colunaRepository.countByProjIdAndColTituloAndColIdNot(colunaExistente.getProjId(),
                    novoTitulo, colunaExistente.getColId());
            if (duplicatas > 0) {
                throw new NomeDeColunaJaExisteException("Título de coluna já existe",
                        "Já existe uma coluna com esse título neste projeto.");
            }
            colunaExistente.setColTitulo(novoTitulo);
        }

        try {
            ColunaModel colunaAtualizada = colunaRepository.save(colunaExistente);
            return new ColunaDTO(colunaAtualizada.getColId(), colunaAtualizada.getColTitulo(),
                    colunaAtualizada.getColOrdem(), colunaAtualizada.getProjId());
        } catch (DuplicateKeyException ex) {
            throw new NomeDeColunaJaExisteException("Título de coluna já existe",
                    "Já existe uma coluna com esse título neste projeto.");
        }
    }

    @Transactional
    public void atualizarOrdemColunas(String projetoId, List<String> colunasIdsOrdenadas) {
        if (colunasIdsOrdenadas == null || colunasIdsOrdenadas.isEmpty()) {
            return;
        }

        List<ColunaModel> colunasParaAtualizar = colunaRepository.findAllById(colunasIdsOrdenadas);

        Map<String, Integer> ordemMap = new HashMap<>();
        for (int i = 0; i < colunasIdsOrdenadas.size(); i++) {
            ordemMap.put(colunasIdsOrdenadas.get(i), i);
        }

        for (ColunaModel coluna : colunasParaAtualizar) {
            if (coluna.getProjId().equals(projetoId)) {
                Integer novaOrdem = ordemMap.get(coluna.getColId());
                if (novaOrdem != null) {
                    coluna.setColOrdem(novaOrdem);
                }
            } else {
                logger.warn("Tentativa de reordenar a coluna {} que não pertence ao projeto {}",
                        coluna.getColId(), projetoId);
            }
        }

        colunaRepository.saveAll(colunasParaAtualizar);
    }
}