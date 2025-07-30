package br.com.dinewise.domain.requests.address;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressRequestTest {

    @Test
    void testAddressRequestFields() {
        AddressRequest request = new AddressRequest(
                "Rua A",
                "123",
                "Apto 1",
                "Centro",
                "Cidade",
                "UF",
                "12345-678"
        );

        assertEquals("Rua A", request.street());
        assertEquals("123", request.houseNumber());
        assertEquals("Apto 1", request.complement());
        assertEquals("Centro", request.neighborhood());
        assertEquals("Cidade", request.city());
        assertEquals("UF", request.state());
        assertEquals("12345-678", request.zipCode());
    }
}