package com.microservice.bank.msavaliadorcredito.application.ex;

public class ErroSolicitacaoCartaoException extends RuntimeException {
    public ErroSolicitacaoCartaoException(String message) {
        super(message);
    }
}
