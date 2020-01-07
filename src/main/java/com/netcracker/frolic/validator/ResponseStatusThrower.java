package com.netcracker.frolic.validator;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Consumer;

public enum ResponseStatusThrower implements Consumer<String> { BAD_REQUEST(HttpStatus.BAD_REQUEST);
    public final HttpStatus status;

    ResponseStatusThrower(HttpStatus status)
    { this.status = status; }

    @Override
    public void accept(String errorMessage) {
        throw new ResponseStatusException(status, errorMessage);
    }
}
