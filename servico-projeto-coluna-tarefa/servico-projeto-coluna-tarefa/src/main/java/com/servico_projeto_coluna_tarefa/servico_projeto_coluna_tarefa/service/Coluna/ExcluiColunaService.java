package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ColunaRepository;

@Service
public class ExcluiColunaService {
    @Autowired
    private ColunaRepository colunaRepository;

    public void deletarColuna(String col_id) {
        if (!colunaRepository.existsById(col_id)) {
            throw new ProjetoNaoEncontradoException("Coluna não encontrada",
                    "Coluna com id " + col_id + " não foi encontrada");
        }
        colunaRepository.deleteById(col_id);
    }
}