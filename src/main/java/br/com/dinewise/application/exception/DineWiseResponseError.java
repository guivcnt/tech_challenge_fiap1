package br.com.dinewise.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DineWiseResponseError extends Exception {

    private final String message;
    private final HttpStatus status;

    public DineWiseResponseError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
