package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.responses.DineWiseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleDineWiseResponseError_deveRetornarStatusECorpoCorretos() {
        DineWiseResponseError ex = new DineWiseResponseError("Erro de negócio", HttpStatus.BAD_REQUEST);

        ResponseEntity<DineWiseResponse> response = handler.handleDineWiseResponseError(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro de negócio", response.getBody().message());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().status());
    }

    @Test
    void handleGenericException_deveRetornarErroDesconhecido500() {
        Exception ex = new Exception("Falha inesperada");

        ResponseEntity<DineWiseResponse> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro desconhecido", response.getBody().message());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().status());
    }

    @Test
    void handleNoResourceFoundException_deveRetornarNotFound() {
        NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET, "/nao-existe");

        ResponseEntity<DineWiseResponse> response = handler.handleNoResourceFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unknown service", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }
}