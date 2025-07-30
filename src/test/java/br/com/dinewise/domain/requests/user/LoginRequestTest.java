package br.com.dinewise.domain.requests.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {

    @Test
    void testLoginRequestFields() {
        LoginRequest request = new LoginRequest("usuario123", "senhaSegura");

        assertEquals("usuario123", request.login());
        assertEquals("senhaSegura", request.password());
    }
}
