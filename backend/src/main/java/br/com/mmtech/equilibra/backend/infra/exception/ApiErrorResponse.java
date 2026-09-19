package br.com.mmtech.equilibra.backend.infra.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        int status,
        String titulo,
        String detalhe,
        String caminho,
        LocalDateTime timestamp,
        List<CampoErro> erros
) {
    public record CampoErro(String campo, String mensagem) {}
}
