package br.com.dinewise.domain.requests.menu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuRequestTest {

    @Test
    void testMenuRequestFields() {
        MenuRequest request = new MenuRequest(
                "Pizza",
                "Pizza de calabresa",
                49.99,
                true,
                "/imagens/pizza.png",
                1L
        );

        assertEquals("Pizza", request.name());
        assertEquals("Pizza de calabresa", request.description());
        assertEquals(49.99, request.price());
        assertTrue(request.onlyForDelivery());
        assertEquals("/imagens/pizza.png", request.imagePath());
        assertEquals(1L, request.restaurantId());
    }
}