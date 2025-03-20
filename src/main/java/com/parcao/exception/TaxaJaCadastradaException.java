package com.parcao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TaxaJaCadastradaException extends RuntimeException {
    public TaxaJaCadastradaException(String message) { super(message); }
}
