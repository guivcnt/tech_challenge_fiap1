package br.com.dinewise.domain.responses;

import org.springframework.http.HttpStatus;

public record DineWiseResponse(String message, HttpStatus status) {
}
