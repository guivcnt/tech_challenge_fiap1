package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.responses.DineWiseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DineWiseResponseError.class)
    public ResponseEntity<DineWiseResponse> handleDineWiseResponseError(DineWiseResponseError ex) {
        DineWiseResponse response = new DineWiseResponse(ex.getMessage(), ex.getStatus());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DineWiseResponse> handleGenericException(Exception ex) {
        DineWiseResponse response = new DineWiseResponse("Erro desconhecido", HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
