package br.com.dinewise.domain.requests.restaurant;

import br.com.dinewise.domain.requests.address.AddressRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantRequestTest {

    @Test
    void testRestaurantRequestFields() {
        AddressRequest address = new AddressRequest(
                "Rua A",
                "123",
                "Apto 1",
                "Centro",
                "Cidade",
                "UF",
                "12345-678"
        );

        RestaurantRequest request = new RestaurantRequest(
                "Restaurante X",
                "Italiana",
                "user123",
                "08:00-22:00",
                address
        );

        assertEquals("Restaurante X", request.name());
        assertEquals("Italiana", request.typeCuisine());
        assertEquals("user123", request.userLoginOwner());
        assertEquals("08:00-22:00", request.operationHours());
        assertEquals(address, request.address());
    }
}