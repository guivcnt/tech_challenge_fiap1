package br.com.dinewise.application.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestaurantEntityTest {

    @Test
    void testGettersAndSetters() {
        RestaurantEntity restaurant = new RestaurantEntity();
        LocalDateTime now = LocalDateTime.now();

        restaurant.setId(1L);
        restaurant.setName("Restaurante Saboroso");
        restaurant.setTypeCuisine("Italiana");
        restaurant.setOperationHours("10:00-22:00");
        restaurant.setUserIdOwner(5L);
        restaurant.setAddressId(100L);
        restaurant.setLastDateModified(now);

        assertEquals(1L, restaurant.getId());
        assertEquals("Restaurante Saboroso", restaurant.getName());
        assertEquals("Italiana", restaurant.getTypeCuisine());
        assertEquals("10:00-22:00", restaurant.getOperationHours());
        assertEquals(5L, restaurant.getUserIdOwner());
        assertEquals(100L, restaurant.getAddressId());
        assertEquals(now, restaurant.getLastDateModified());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        RestaurantEntity restaurant = new RestaurantEntity(
                2L, "Churrascaria Boi Gordo", "Churrasco", "11:00-23:00", 10L, 200L, now
        );

        assertEquals(2L, restaurant.getId());
        assertEquals("Churrascaria Boi Gordo", restaurant.getName());
        assertEquals("Churrasco", restaurant.getTypeCuisine());
        assertEquals("11:00-23:00", restaurant.getOperationHours());
        assertEquals(10L, restaurant.getUserIdOwner());
        assertEquals(200L, restaurant.getAddressId());
        assertEquals(now, restaurant.getLastDateModified());
    }

    @Test
    void testNoArgsConstructor() {
        RestaurantEntity restaurant = new RestaurantEntity();
        assertNotNull(restaurant);
    }
}