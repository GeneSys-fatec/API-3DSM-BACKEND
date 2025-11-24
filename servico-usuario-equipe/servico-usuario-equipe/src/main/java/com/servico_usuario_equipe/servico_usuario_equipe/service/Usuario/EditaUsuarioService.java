package com.servico_usuario_equipe.servico_usuario_equipe.service.Usuario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.autenticação.SenhaIncorretaException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.usuário.EmailJaCadastradoException;
import com.servico_usuario_equipe.servico_usuario_equipe.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.servico_usuario_equipe.servico_usuario_equipe.model.entidade.UsuarioModel;
import com.servico_usuario_equipe.servico_usuario_equipe.repository.UsuarioRepository;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Auth.CookieService;
import com.servico_usuario_equipe.servico_usuario_equipe.service.Token.CriaTokenService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class EditaUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private CriaTokenService criaTokenService;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private FotoStorageService fotoStorageService;

    public UsuarioModel atualizarNomeEmail(String id, String nome, String email, HttpServletResponse response) {
    UsuarioModel usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado", "O usuário com o ID fornecido não existe."));

        usuarioRepository.findByEmail(email).filter(u -> !u.getUsuId().equals(id))
            .ifPresent(u -> {
                throw new EmailJaCadastradoException("Email já em uso", "O email fornecido já está cadastrado para outro usuário.");
            });

        usuario.setUsuNome(nome);
        usuario.setUsuEmail(email);
        
        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuario); 
        String novoToken = criaTokenService.generateToken(usuarioAtualizado);
        ResponseCookie novoCookie = cookieService.createJWTCookie(novoToken);
        response.addHeader(HttpHeaders.SET_COOKIE, novoCookie.toString());

        return usuarioAtualizado;
    }

    public void atualizarSenha(String id, String senhaAtual, String novaSenha) {
        UsuarioModel usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado", "O usuário com o ID fornecido não existe."));

        if (!encoder.matches(senhaAtual, usuario.getUsuSenha())) {
            throw new SenhaIncorretaException("Senha atual incorreta", "A senha atual fornecida não corresponde à senha registrada.");
        }

        usuario.setUsuSenha(encoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizarFoto(String id, MultipartFile arquivo) throws IOException {

        UsuarioModel usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado", "O usuário com o ID fornecido não existe."));

        String nomeArquivoOriginal = arquivo.getOriginalFilename();
        String extensao = "";
        if (nomeArquivoOriginal != null && nomeArquivoOriginal.contains(".")) {
            extensao = nomeArquivoOriginal.substring(nomeArquivoOriginal.lastIndexOf("."));
        }

        String novoNomeArquivo = id + extensao; 
        String caminhoRelativoSalvo = "fotos/" + novoNomeArquivo;

        Path destinoPath = fotoStorageService.getFotoPath(caminhoRelativoSalvo);
        Files.createDirectories(destinoPath.getParent());
         
        String nomeFotoUnico = id + extensao; 
        String caminhoParaStorage = "fotos/" + nomeFotoUnico;
        
        Path destinoPathStorage = fotoStorageService.getFotoPath(caminhoParaStorage);
        
        Files.createDirectories(destinoPathStorage.getParent());
        arquivo.transferTo(destinoPathStorage.toFile());
        usuario.setUsuCaminhoFoto(nomeFotoUnico); 
        
        return usuarioRepository.save(usuario);
    }
}
