package br.com.mmtech.equilibra.backend.email.service;

public interface EmailService {

    void enviarEmailConvite(String destinatario, String token);
}
