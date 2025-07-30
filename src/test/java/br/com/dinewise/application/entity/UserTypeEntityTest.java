package br.com.dinewise.application.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTypeEntityTest {

    @Test
    void testGettersAndSetters() {
        UserTypeEntity userType = new UserTypeEntity();
        LocalDateTime now = LocalDateTime.now();

        userType.setId(1L);
        userType.setType("ADMIN");
        userType.setLastDateModified(now);

        assertEquals(1L, userType.getId());
        assertEquals("ADMIN", userType.getType());
        assertEquals(now, userType.getLastDateModified());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        UserTypeEntity userType = new UserTypeEntity(2L, "CLIENT", now);

        assertEquals(2L, userType.getId());
        assertEquals("CLIENT", userType.getType());
        assertEquals(now, userType.getLastDateModified());
    }

    @Test
    void testNoArgsConstructor() {
        UserTypeEntity userType = new UserTypeEntity();
        assertNotNull(userType);
    }
}