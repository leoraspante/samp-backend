// Exceção lançada quando a autenticação por PIN falha (PIN inválido ou usuário inativo).

package com.improel.samp.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
