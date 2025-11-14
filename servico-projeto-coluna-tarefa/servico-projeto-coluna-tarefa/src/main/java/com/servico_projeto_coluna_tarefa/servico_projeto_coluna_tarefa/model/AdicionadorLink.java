package com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model;

import java.util.List;

import com.servico_projeto_coluna_tarefa.servico_projeto_coluna_tarefa.model.entidade.TarefaModel;

public interface AdicionadorLink<Tarefa> {
	public void adicionarLink(List<TarefaModel> lista);

	public void adicionarLink(TarefaModel objeto);
}