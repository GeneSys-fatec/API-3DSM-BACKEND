package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.service.Projeto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.ProjetoModel;
import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.repository.ProjetoRepository;

@Service
public class CriaProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private CriaColunasPadraoProjetoService criaColunasPadraoProjetoService;

    

    public ProjetoModel criarNovoProjeto(ProjetoModel projeto, String equId) {
        
        projeto.setEquId(equId); 

        ProjetoModel projetoSalvo = projetoRepository.save(projeto);

        
        criaColunasPadraoProjetoService.criarColunasPadraoParaProjeto(projetoSalvo.getProjId());

        return projetoSalvo;
    }
}