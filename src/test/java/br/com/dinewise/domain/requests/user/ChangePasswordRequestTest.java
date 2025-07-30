package br.com.dinewise.domain.requests.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangePasswordRequestTest {

    @Test
    void testChangePasswordRequestFields() {
        ChangePasswordRequest request = new ChangePasswordRequest("senhaAntiga", "senhaNova");

        assertEquals("senhaAntiga", request.oldPassword());
        assertEquals("senhaNova", request.newPassword());
    }
}