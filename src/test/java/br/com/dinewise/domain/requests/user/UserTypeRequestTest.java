package br.com.dinewise.domain.requests.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTypeRequestTest {

    @Test
    void testUserTypeRequestField() {
        UserTypeRequest request = new UserTypeRequest("ADMIN");

        assertEquals("ADMIN", request.userType());
    }
}