package com.servico_usuario_equipe.servico_usuario_equipe.service.Equipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.EquipeModel;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.EquipeRepository;

@Service
public class ExcluiEquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private BuscaEquipeService buscaEquipeService;
    @Autowired
    private ValidaEquipeService validacaoEquipeService;

    public void excluir(String id, UsuarioModel usuarioLogado) {
        EquipeModel equipe = buscaEquipeService.getEquipeById(id);
        validacaoEquipeService.verificarSeUsuarioPodeExcluirEquipe(equipe, usuarioLogado);
        equipeRepository.delete(equipe);
    }
}