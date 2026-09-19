package br.com.mmtech.equilibra.backend.infra.exception;

import br.com.mmtech.equilibra.backend.comum.exception.EntidadeNaoEncontradaException;
import br.com.mmtech.equilibra.backend.comum.exception.NegocioException;
import br.com.mmtech.equilibra.backend.comum.exception.OperacaoNaoPermitidaException;
import br.com.mmtech.equilibra.backend.convite.exception.ConviteInvalidoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String FIELD_TIMESTAMP = "timestamp";

    @ExceptionHandler({EntityNotFoundException.class, EntidadeNaoEncontradaException.class})
    public ProblemDetail handleEntityNotFoundException(RuntimeException ex, HttpServletRequest request) {
        log.warn("Recurso não encontrado: uri={} | detalhe={}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Recurso Não Encontrado");
        problem.setType(URI.create("https://equilibra.com.br/erros/recurso-nao-encontrado"));
        problem.setInstance(URI.create(request.getRequestURI()));
        adicionarMetadadosPadrao(problem);
        return problem;
    }

    @ExceptionHandler(ConviteInvalidoException.class)
    public ProblemDetail handleConviteInvalido(ConviteInvalidoException ex, HttpServletRequest request) {
        log.warn("Operação de convite recusada: uri={} | erro={}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
        problem.setTitle("Convite Inválido ou Expirado");
        problem.setType(URI.create("https://equilibra.com.br/erros/convite-invalido"));
        problem.setInstance(URI.create(request.getRequestURI()));
        adicionarMetadadosPadrao(problem);
        return problem;
    }

    @ExceptionHandler({OperacaoNaoPermitidaException.class, AccessDeniedException.class})
    public ProblemDetail handleAcessoNegado(Exception ex, HttpServletRequest request) {
        log.warn("Acesso negado: uri={} | erro={}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problem.setTitle("Operação Não Permitida");
        problem.setType(URI.create("https://equilibra.com.br/erros/acesso-negado"));
        problem.setInstance(URI.create(request.getRequestURI()));
        adicionarMetadadosPadrao(problem);
        return problem;
    }

    @ExceptionHandler(NegocioException.class)
    public ProblemDetail handleNegocio(NegocioException ex, HttpServletRequest request) {
        log.warn("Violação de regra de negócio: uri={} | erro={}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Regra de Negócio Violada");
        problem.setType(URI.create("https://equilibra.com.br/erros/regra-de-negocio"));
        problem.setInstance(URI.create(request.getRequestURI()));
        adicionarMetadadosPadrao(problem);
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Falha de validação nos campos: uri={}", request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos contêm valores inválidos."
        );
        problem.setTitle("Dados de Entrada Inválidos");
        problem.setType(URI.create("https://equilibra.com.br/erros/validacao-campos"));
        problem.setInstance(URI.create(request.getRequestURI()));

        Map<String, String> errosCampos = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Valor inválido",
                        (existente, _) -> existente
                ));

        problem.setProperty("invalidParams", errosCampos);
        adicionarMetadadosPadrao(problem);
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUncaught(Exception ex, HttpServletRequest request) {
        log.error("Erro interno não tratado: uri={} | mensagem={}", request.getRequestURI(), ex.getMessage(), ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado no servidor."
        );
        problem.setTitle("Erro Interno do Servidor");
        problem.setType(URI.create("https://equilibra.com.br/erros/erro-interno"));
        problem.setInstance(URI.create(request.getRequestURI()));
        adicionarMetadadosPadrao(problem);
        return problem;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Parâmetro obrigatório ausente: " + ex.getParameterName()
        );
        problem.setTitle("Parâmetro Ausente");
        problem.setType(URI.create("https://equilibra.com.br/erros/parametro-ausente"));
        problem.setInstance(URI.create(request.getRequestURI()));
        adicionarMetadadosPadrao(problem);
        return problem;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("Falha de autenticação: uri={} | erro={}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "E-mail ou senha inválidos."
        );
        problem.setTitle("Credenciais Inválidas");
        problem.setType(URI.create("https://equilibra.com.br/erros/credenciais-invalidas"));
        problem.setInstance(URI.create(request.getRequestURI()));
        adicionarMetadadosPadrao(problem);
        return problem;
    }

    private void adicionarMetadadosPadrao(ProblemDetail problem) {
        problem.setProperty("timestamp", Instant.now());
        String traceId = MDC.get("traceId");
        if (traceId != null) {
            problem.setProperty("traceId", traceId);
        }
    }
}
