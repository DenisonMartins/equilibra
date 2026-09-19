package br.com.mmtech.equilibra.backend.comum.exception;

public class OperacaoNaoPermitidaException extends NegocioException {

    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
