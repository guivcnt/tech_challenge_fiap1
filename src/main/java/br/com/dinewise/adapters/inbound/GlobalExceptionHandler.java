package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.responses.DineWiseResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(DineWiseResponseError.class)
    public ResponseEntity<DineWiseResponse> handleDineWiseResponseError(DineWiseResponseError ex) {
        DineWiseResponse response = new DineWiseResponse(ex.getMessage(), ex.getStatus());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DineWiseResponse> handleGenericException(Exception ex) {
        log.error("Erro inesperado: ", ex);
        DineWiseResponse response = new DineWiseResponse("Erro desconhecido", HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<DineWiseResponse> handleNoResourceFoundException(Exception ex) {
        DineWiseResponse response = new DineWiseResponse("Unknown service", HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
