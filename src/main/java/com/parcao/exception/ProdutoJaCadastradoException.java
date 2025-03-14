package com.parcao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ProdutoJaCadastradoException extends RuntimeException {

    public ProdutoJaCadastradoException(String message) { super(message); }
}
