package br.com.dinewise.application.exception;

import org.springframework.http.HttpStatus;

public class DineWiseResponseError extends Exception {

    private final String message;
    private final HttpStatus status;

    public DineWiseResponseError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
