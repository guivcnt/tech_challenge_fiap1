package br.com.dinewise.application.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserEntityTest {

    @Test
    void testGettersAndSetters() {
        UserEntity user = new UserEntity();
        LocalDateTime now = LocalDateTime.now();

        user.setId(1L);
        user.setName("João Silva");
        user.setEmail("joao@email.com");
        user.setLogin("joaosilva");
        user.setPassword("senha123");
        user.setUserType("ADMIN");
        user.setAddressId(10L);
        user.setLastDateModified(now);

        assertEquals(1L, user.getId());
        assertEquals("João Silva", user.getName());
        assertEquals("joao@email.com", user.getEmail());
        assertEquals("joaosilva", user.getLogin());
        assertEquals("senha123", user.getPassword());
        assertEquals("ADMIN", user.getUserType());
        assertEquals(10L, user.getAddressId());
        assertEquals(now, user.getLastDateModified());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        UserEntity user = new UserEntity(
                2L, "Maria Souza", "maria@email.com", "mariasouza", "senha456", "CLIENT", 20L, now
        );

        assertEquals(2L, user.getId());
        assertEquals("Maria Souza", user.getName());
        assertEquals("maria@email.com", user.getEmail());
        assertEquals("mariasouza", user.getLogin());
        assertEquals("senha456", user.getPassword());
        assertEquals("CLIENT", user.getUserType());
        assertEquals(20L, user.getAddressId());
        assertEquals(now, user.getLastDateModified());
    }

    @Test
    void testNoArgsConstructor() {
        UserEntity user = new UserEntity();
        assertNotNull(user);
    }
}