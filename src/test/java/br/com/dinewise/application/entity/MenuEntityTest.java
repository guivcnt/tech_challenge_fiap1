package br.com.dinewise.application.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MenuEntityTest {

    @Test
    void testGettersAndSetters() {
        MenuEntity menu = new MenuEntity();
        LocalDateTime now = LocalDateTime.now();

        menu.setId(1L);
        menu.setName("Pizza");
        menu.setDescription("Pizza de calabresa");
        menu.setPrice(49.90);
        menu.setOnlyForDelivery(true);
        menu.setImagePath("/imagens/pizza.png");
        menu.setRestaurantId(100L);
        menu.setLastDateModified(now);

        assertEquals(1L, menu.getId());
        assertEquals("Pizza", menu.getName());
        assertEquals("Pizza de calabresa", menu.getDescription());
        assertEquals(49.90, menu.getPrice());
        assertTrue(menu.getOnlyForDelivery());
        assertEquals("/imagens/pizza.png", menu.getImagePath());
        assertEquals(100L, menu.getRestaurantId());
        assertEquals(now, menu.getLastDateModified());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        MenuEntity menu = new MenuEntity(
                2L, "Hambúrguer", "Hambúrguer artesanal", 29.90, false, "/imagens/burger.png", 200L, now
        );

        assertEquals(2L, menu.getId());
        assertEquals("Hambúrguer", menu.getName());
        assertEquals("Hambúrguer artesanal", menu.getDescription());
        assertEquals(29.90, menu.getPrice());
        assertFalse(menu.getOnlyForDelivery());
        assertEquals("/imagens/burger.png", menu.getImagePath());
        assertEquals(200L, menu.getRestaurantId());
        assertEquals(now, menu.getLastDateModified());
    }

    @Test
    void testNoArgsConstructor() {
        MenuEntity menu = new MenuEntity();
        assertNotNull(menu);
    }
}