package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.AddressEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.address.AddressRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressRepositoryImplTest {

    @Test
    void testCreateSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepositoryImpl repository = new AddressRepositoryImpl(jdbcClient);

        AddressRequest request = new AddressRequest(
                "Rua A", "123", "Apto 1", "Centro", "Cidade", "Estado", "12345-678"
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        keyHolder.getKeyList().add(keys);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
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

        AddressEntity entity = repository.create(request);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Rua A", entity.getPublicPlace());
        assertEquals("123", entity.getHouseNumber());
        assertEquals("Apto 1", entity.getComplement());
        assertEquals("Centro", entity.getNeighborhood());
        assertEquals("Cidade", entity.getCity());
        assertEquals("Estado", entity.getState());
        assertEquals("12345-678", entity.getZipCode());
        assertNotNull(entity.getLastDateModified());
    }

    @Test
    void testCreateThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepositoryImpl repository = new AddressRepositoryImpl(jdbcClient);

        AddressRequest request = new AddressRequest(
                "Rua A", "123", "Apto 1", "Centro", "Cidade", "Estado", "12345-678"
        );

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update(any(KeyHolder.class)))
                .thenThrow(new RuntimeException("DB error"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.create(request));
        assertEquals("Error creating address", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testGetSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepositoryImpl repository = new AddressRepositoryImpl(jdbcClient);

        AddressEntity entity = new AddressEntity(1L, "Rua A", "123", "Apto 1", "Centro", "Cidade", "Estado", "12345-678", null);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .query(AddressEntity.class)
                .optional())
                .thenReturn(Optional.of(entity));

        Optional<AddressEntity> result = repository.get(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Rua A", result.get().getPublicPlace());
    }

    @Test
    void testGetThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepositoryImpl repository = new AddressRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .query(AddressEntity.class)
                .optional())
                .thenThrow(new RuntimeException("DB error"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.get(1L));
        assertEquals("Error get address", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testDeleteSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepositoryImpl repository = new AddressRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .update())
                .thenReturn(1);

        Optional<AddressEntity> result = repository.delete(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testDeleteThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepositoryImpl repository = new AddressRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .update())
                .thenThrow(new RuntimeException("DB error"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.delete(1L));
        assertEquals("Error deleting address", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

}