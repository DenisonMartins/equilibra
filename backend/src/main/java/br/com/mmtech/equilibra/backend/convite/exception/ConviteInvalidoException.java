package br.com.mmtech.equilibra.backend.convite.exception;

import br.com.mmtech.equilibra.backend.comum.exception.NegocioException;

public class ConviteInvalidoException extends NegocioException {
    public ConviteInvalidoException(String message) {
        super(message);
    }
}
