package com.servico_usuario_equipe.servico_usuario_equipe.service.Equipe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.CriadorNaoPodeSairException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.equipes.UsuarioNaoEMembroException;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.EquipeModel;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.EquipeRepository;

@Service
public class SairDaEquipeService {

    @Autowired
    private BuscaEquipeService buscaEquipeService;

    @Autowired
    private EquipeRepository equipeRepository;

    public void sair(String equipeId, UsuarioModel usuarioLogado) {

        EquipeModel equipe = buscaEquipeService.getEquipeById(equipeId);
        if (equipe.getCriadorId().equals(usuarioLogado.getUsuId())) {
            throw new CriadorNaoPodeSairException("Ação Inválida", "O criador não pode sair da própria equipe. Considere excluir a equipe ou transferir a propriedade.");
        }

        List<UsuarioModel> membros = equipe.getUsuarios();
        boolean foiRemovido = membros.removeIf(membro -> membro.getUsuId().equals(usuarioLogado.getUsuId()));
        if (!foiRemovido) {
            throw new UsuarioNaoEMembroException("Ação Inválida", "Você não pode sair de uma equipe da qual não é membro.");
        }
        equipeRepository.save(equipe);
    }
}