package br.com.dinewise.domain.responses;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DineWiseResponseTest {

    @Test
    void testDineWiseResponseFields() {
        DineWiseResponse response = new DineWiseResponse("Operação realizada com sucesso", HttpStatus.OK);

        assertEquals("Operação realizada com sucesso", response.message());
        assertEquals(HttpStatus.OK, response.status());
    }
}