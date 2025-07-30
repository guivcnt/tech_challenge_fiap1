package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.MenuEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.menu.MenuRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuRepositoryImplTest {

    @Test
    void testCreateSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        MenuRequest request = new MenuRequest("Pizza", "Deliciosa", 50.0, true, "img.png", 1L);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 10L);
        keyHolder.getKeyList().add(keys);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update(any(KeyHolder.class)))
                .then(invocation -> {
                    KeyHolder kh = invocation.getArgument(0);
                    kh.getKeyList().add(keys);
                    return 1;
                });

        MenuEntity entity = repository.create(request);

        assertNotNull(entity);
        assertEquals(10L, entity.getId());
        assertEquals("Pizza", entity.getName());
        assertEquals("Deliciosa", entity.getDescription());
        assertEquals(50.0, entity.getPrice());
        assertEquals(true, entity.getOnlyForDelivery());
        assertEquals("img.png", entity.getImagePath());
        assertEquals(1L, entity.getRestaurantId());
        assertNotNull(entity.getLastDateModified());
    }

    @Test
    void testCreateThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        MenuRequest request = new MenuRequest("Pizza", "Deliciosa", 50.0, true, "img.png", 1L);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any()))
                .thenThrow(new RuntimeException("DB error"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.create(request));
        assertEquals("Error creating menu item", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testGetAllSuccess() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        List<MenuEntity> menus = List.of(new MenuEntity(1L, "Pizza", "desc", 10.0, true, "img", 1L, LocalDateTime.now()));
        when(jdbcClient.sql(anyString()).query(MenuEntity.class).list()).thenReturn(menus);

        List<MenuEntity> result = repository.getAll();

        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());
    }

    @Test
    void testGetSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        MenuEntity entity = new MenuEntity(1L, "Pizza", "desc", 10.0, true, "img", 1L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(MenuEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<MenuEntity> result = repository.get(1L);

        assertTrue(result.isPresent());
        assertEquals("Pizza", result.get().getName());
    }

    @Test
    void testGetThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(MenuEntity.class).optional())
                .thenThrow(new RuntimeException("DB error"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.get(1L));
        assertEquals("Error get menu item", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testUpdateSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        MenuRequest request = new MenuRequest("Pizza", "desc", 10.0, true, "img", 1L);
        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(1);

        Optional<MenuEntity> result = repository.update(1L, request);

        assertTrue(result.isPresent());
        assertEquals("Pizza", result.get().getName());
    }

    @Test
    void testUpdateNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        MenuRequest request = new MenuRequest("Pizza", "desc", 10.0, true, "img", 1L);
        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(0);

        Optional<MenuEntity> result = repository.update(1L, request);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        MenuRequest request = new MenuRequest("Pizza", "desc", 10.0, true, "img", 1L);
        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenThrow(new RuntimeException("DB error"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.update(1L, request));
        assertEquals("Error updating menu item", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testDeleteSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenReturn(1);

        Optional<MenuEntity> result = repository.delete(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testDeleteNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenReturn(0);

        Optional<MenuEntity> result = repository.delete(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        MenuRepositoryImpl repository = new MenuRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenThrow(new RuntimeException("DB error"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.delete(1L));
        assertEquals("Error deleting menu item", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }
}