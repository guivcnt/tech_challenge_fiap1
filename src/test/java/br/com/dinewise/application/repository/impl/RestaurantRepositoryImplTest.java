package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.AddressEntity;
import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.AddressRepository;
import br.com.dinewise.domain.requests.address.AddressRequest;
import br.com.dinewise.domain.requests.restaurant.RestaurantRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
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

class RestaurantRepositoryImplTest {

    @Test
    void testCreateSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        RestaurantRequest request = new RestaurantRequest("Restaurante", "Italiana", "user", "08-18", addressRequest);

        AddressEntity addressEntity = new AddressEntity(5L, "Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000", LocalDateTime.now());
        when(addressRepository.create(addressRequest)).thenReturn(addressEntity);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 99L);
        keyHolder.getKeyList().add(keys);

        when(jdbcClient.sql(anyString())
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

        RestaurantEntity entity = repository.create(request, 7L);

        assertNotNull(entity);
        assertEquals(99L, entity.getId());
        assertEquals("Restaurante", entity.getName());
        assertEquals("Italiana", entity.getTypeCuisine());
        assertEquals("08-18", entity.getOperationHours());
        assertEquals(7L, entity.getUserIdOwner());
        assertEquals(5L, entity.getAddressId());
        assertNotNull(entity.getLastDateModified());
    }

    @Test
    void testCreateThrowsDuplicateKey() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        RestaurantRequest request = new RestaurantRequest("Restaurante", "Italiana", "user", "08-18", addressRequest);

        when(addressRepository.create(addressRequest)).thenReturn(new AddressEntity(1L, null, null, null, null, null, null, null, null));

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update(any(KeyHolder.class)))
                .thenThrow(new DuplicateKeyException("dup"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.create(request, 1L));
        assertEquals("Restaurant already exists", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    @SneakyThrows
    void testCreateThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        RestaurantRequest request = new RestaurantRequest("Restaurante", "Italiana", "user", "08-18", addressRequest);

        when(addressRepository.create(addressRequest)).thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.create(request, 1L));
        assertEquals("Error creating restaurant", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testGetAllSuccess() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        List<RestaurantEntity> list = List.of(new RestaurantEntity(1L, "R", "T", "H", 2L, 3L, LocalDateTime.now()));
        when(jdbcClient.sql(anyString()).query(RestaurantEntity.class).list()).thenReturn(list);

        List<RestaurantEntity> result = repository.getAll();
        assertEquals(1, result.size());
        assertEquals("R", result.get(0).getName());
    }

    @Test
    void testGetSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        RestaurantEntity entity = new RestaurantEntity(1L, "R", "T", "H", 2L, 3L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(RestaurantEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<RestaurantEntity> result = repository.get(1L);
        assertTrue(result.isPresent());
        assertEquals("R", result.get().getName());
    }

    @Test
    void testGetThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(RestaurantEntity.class).optional())
                .thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.get(1L));
        assertEquals("Error get restaurant", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    @SneakyThrows
    void testUpdateSuccess() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        RestaurantRequest request = new RestaurantRequest("Restaurante", "Italiana", "user", "08-18", addressRequest);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(1);

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

        Optional<RestaurantEntity> result = repository.update(1L, request, 2L);
        assertTrue(result.isPresent());
        assertEquals("Restaurante", result.get().getName());
        assertEquals(2L, result.get().getUserIdOwner());
    }

    @Test
    void testUpdateNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        RestaurantRequest request = new RestaurantRequest("Restaurante", "Italiana", "user", "08-18", addressRequest);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(0);

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

        Optional<RestaurantEntity> result = repository.update(1L, request, 2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        RestaurantRequest request = new RestaurantRequest("Restaurante", "Italiana", "user", "08-18", addressRequest);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.update(1L, request, 2L));
        assertEquals("Error updating restaurant", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testDeleteSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        RestaurantEntity entity = new RestaurantEntity(1L, "R", "T", "H", 2L, 3L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(RestaurantEntity.class).optional())
                .thenReturn(Optional.of(entity));

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenReturn(1);

        when(addressRepository.delete(3L)).thenReturn(Optional.of(mock(AddressEntity.class)));

        Optional<RestaurantEntity> result = repository.delete(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testDeleteNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        RestaurantEntity entity = new RestaurantEntity(1L, "R", "T", "H", 2L, 3L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(RestaurantEntity.class).optional())
                .thenReturn(Optional.of(entity));

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenReturn(0);

        Optional<RestaurantEntity> result = repository.delete(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(RestaurantEntity.class).optional())
                .thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.delete(1L));
        assertEquals("Error deleting restaurant", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testDeleteRestaurantNotFound() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        RestaurantRepositoryImpl repository = new RestaurantRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(RestaurantEntity.class).optional())
                .thenReturn(Optional.empty());

        Optional<RestaurantEntity> result = repository.delete(1L);

        assertTrue(result.isEmpty());
    }
}