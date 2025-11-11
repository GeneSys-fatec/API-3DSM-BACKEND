package com.servico_usuario_equipe.servico_usuario_equipe.service.Equipe;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servico_usuario_equipe.servico_usuario_equipe.model.dto.EquipeDTO;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.EquipeModel;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.EquipeRepository;
// import com.servico_usuario_equipe.servico_usuario_equipe.service.Notificacao.CriaNotificacaoService;

@Service
public class CriaEquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private ValidaEquipeService validacaoEquipeService;
    // @Autowired
    // private CriaNotificacaoService criaNotificacaoService;

    public EquipeModel criar(EquipeDTO dto, UsuarioModel criador) {
        validacaoEquipeService.validarNomeUnico(dto.getEquNome(), null);
        List<UsuarioModel> outrosMembros = validacaoEquipeService.buscarEValidarMembrosPorEmails(dto.getMembrosEmails());
        outrosMembros.remove(criador);

        List<UsuarioModel> membros = new ArrayList<>();
        membros.add(criador);
        membros.addAll(outrosMembros);

        EquipeModel equipe = new EquipeModel();
        equipe.setEquNome(dto.getEquNome());
        equipe.setEquDescricao(dto.getEquDescricao());
        equipe.setCriadorId(criador.getUsuId());
        equipe.setUsuarios(membros);

        EquipeModel equipeSalva = equipeRepository.save(equipe);

        // for (UsuarioModel membro : outrosMembros) {
        //     criaNotificacaoService.criarNotificacaoAdicaoEquipe(
        //         criador.getUsuId(),
        //         membro.getUsuId(),
        //         equipeSalva.getEquNome(),
        //         criador.getUsuNome()
        //     );
        // }

        return equipeSalva;
    }
}