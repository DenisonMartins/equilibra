package br.com.mmtech.equilibra.backend.email.service.impl;

import br.com.mmtech.equilibra.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.mail.from}")
    private String remetente;

    @Override
    @Async
    public void enviarEmailConvite(String destinatario, String hash) {
        try {
            String linkAtivacao = frontendUrl + "/convite?hash=" + hash;

            log.info("Iniciando disparo de convite para: {}", destinatario);

            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject("Equilibra - Convite para Acesso ao Sistema");
            mensagem.setText("""
                    Olá
                    
                    Você recebeu um convite para acessar o sistema Equilibra. Uma plataforma de gestão financeira.
                    
                    Para criar sua senha e ativar sua conta, clique no link abaixo:
                    %s
                    
                    Este convite é de uso único e expira em 48 horas.
                    
                    Atenciosamente,
                    Equipe Equilibra
                    """.formatted(linkAtivacao));

            mailSender.send(mensagem);
            log.info("Email de convite enviado com sucesso para: {}", destinatario);
        } catch (Exception e) {
            log.error("Falha ao enviar email de convite para {}: {}", destinatario, e.getMessage());
        }
    }
}
