package com.servico_usuario_equipe.servico_usuario_equipe.service.ResetaSenha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetTokenEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Recuperação de Senha");
        message.setText("Para redefinir sua senha, clique no link: " 
          + "http://localhost:5173/recuperacao-senha?token=" + token);
        
        mailSender.send(message);
    }
}
