package br.com.dinewise.domain.requests.user;

import br.com.dinewise.domain.requests.address.AddressRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRequestTest {

    @Test
    void testUserRequestFields() {
        AddressRequest address = new AddressRequest(
                "Rua A",
                "123",
                "Apto 1",
                "Centro",
                "Cidade",
                "UF",
                "12345-678"
        );

        UserRequest request = new UserRequest(
                "João Silva",
                "joao@email.com",
                "joaosilva",
                "senha123",
                "CLIENTE",
                address
        );

        assertEquals("João Silva", request.name());
        assertEquals("joao@email.com", request.email());
        assertEquals("joaosilva", request.login());
        assertEquals("senha123", request.password());
        assertEquals("CLIENTE", request.userType());
        assertEquals(address, request.address());
    }
}