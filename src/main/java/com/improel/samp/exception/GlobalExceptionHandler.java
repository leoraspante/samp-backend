// Manipulador global de exceções da API REST.
// Centraliza o tratamento de erros e devolve respostas padronizadas (RFC 7807 - ProblemDetail).

package com.improel.samp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 401 Unauthorized - Falha de autenticação (ex.: PIN inválido ou operador
    // inativo).
    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, "Não autorizado", ex.getMessage());
    }

    // 400 Bad Request - Dados inválidos ou regras de entrada violadas.
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Requisição inválida", ex.getMessage());
    }

    // 409 Conflict - Conflito de estado no fluxo de produção (ex.: bancada já
    // ocupada).
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT, "Conflito de estado", ex.getMessage());
    }

    // 400 Bad Request - Erros de validação automática (@Valid nos DTOs).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Um ou mais campos estão inválidos.");

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        problem.setProperty("errors", fieldErrors);

        return problem;
    }

    // 500 Internal Server Error - Fallback para erros inesperados (sem expor
    // detalhes internos).
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente ou acione o suporte.");
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
