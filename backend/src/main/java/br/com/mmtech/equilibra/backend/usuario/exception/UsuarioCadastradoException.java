package br.com.mmtech.equilibra.backend.usuario.exception;

import br.com.mmtech.equilibra.backend.comum.exception.NegocioException;

public class UsuarioCadastradoException extends NegocioException {
    public UsuarioCadastradoException(String message) {
        super(message);
    }
}
