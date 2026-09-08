package br.com.mmtech.equilibra.backend.email.service;

public interface EmailService {

    void enviarEmailConvite(String email, String token);
}
