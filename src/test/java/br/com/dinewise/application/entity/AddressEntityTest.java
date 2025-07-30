package br.com.dinewise.application.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddressEntityTest {

    @Test
    void testGettersAndSetters() {
        AddressEntity address = new AddressEntity();
        LocalDateTime now = LocalDateTime.now();

        address.setId(10L);
        address.setPublicPlace("Rua das Flores");
        address.setHouseNumber("123");
        address.setComplement("Apto 45");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setZipCode("01000-000");
        address.setLastDateModified(now);

        assertEquals(10L, address.getId());
        assertEquals("Rua das Flores", address.getPublicPlace());
        assertEquals("123", address.getHouseNumber());
        assertEquals("Apto 45", address.getComplement());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("São Paulo", address.getCity());
        assertEquals("SP", address.getState());
        assertEquals("01000-000", address.getZipCode());
        assertEquals(now, address.getLastDateModified());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AddressEntity address = new AddressEntity(
                1L, "Av. Brasil", "100", "Casa", "Jardim", "Rio de Janeiro", "RJ", "20000-000", now
        );

        assertEquals(1L, address.getId());
        assertEquals("Av. Brasil", address.getPublicPlace());
        assertEquals("100", address.getHouseNumber());
        assertEquals("Casa", address.getComplement());
        assertEquals("Jardim", address.getNeighborhood());
        assertEquals("Rio de Janeiro", address.getCity());
        assertEquals("RJ", address.getState());
        assertEquals("20000-000", address.getZipCode());
        assertEquals(now, address.getLastDateModified());
    }

    @Test
    void testNoArgsConstructor() {
        AddressEntity address = new AddressEntity();
        assertNotNull(address);
    }
}