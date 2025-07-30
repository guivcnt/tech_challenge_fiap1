package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.user.UserTypeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
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

class UserTypeRepositoryImplTest {

    @Test
    void testCreateSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 123L);
        keyHolder.getKeyList().add(keys);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update(any(KeyHolder.class)))
                .then(invocation -> {
                    KeyHolder kh = invocation.getArgument(0);
                    kh.getKeyList().add(keys);
                    return 1;
                });

        UserTypeEntity entity = repository.create(request);

        assertNotNull(entity);
        assertEquals(123L, entity.getId());
        assertEquals("ADMIN", entity.getType());
        assertNotNull(entity.getLastDateModified());
    }

    @Test
    void testCreateThrowsDuplicateKey() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update(any(KeyHolder.class)))
                .thenThrow(new DuplicateKeyException("dup"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.create(request));
        assertEquals("User type already exists", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void testCreateThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update(any(KeyHolder.class)))
                .thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.create(request));
        assertEquals("Error creating user type", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testReadSuccess() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");
        UserTypeEntity entity = new UserTypeEntity(1L, "ADMIN", LocalDateTime.now());

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserTypeEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<UserTypeEntity> result = repository.read(request);

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getType());
    }

    @Test
    void testReadEmpty() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserTypeEntity.class).optional())
                .thenReturn(Optional.empty());

        Optional<UserTypeEntity> result = repository.read(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void testReadAllSuccess() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        List<UserTypeEntity> list = List.of(new UserTypeEntity(1L, "ADMIN", LocalDateTime.now()));
        when(jdbcClient.sql(anyString()).query(UserTypeEntity.class).list()).thenReturn(list);

        List<UserTypeEntity> result = repository.readAll();

        assertEquals(1, result.size());
        assertEquals("ADMIN", result.get(0).getType());
    }

    @Test
    void testUpdateSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(1);

        UserTypeEntity entity = new UserTypeEntity(1L, "ADMIN", LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserTypeEntity.class).optional())
                .thenReturn(Optional.of(entity));

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserTypeEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<UserTypeEntity> result = repository.update(1L, request);

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getType());
    }

    @Test
    void testUpdateNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(0);

        Optional<UserTypeEntity> result = repository.update(1L, request);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateThrowsDuplicateKey() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenThrow(new DuplicateKeyException("dup"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.update(1L, request));
        assertEquals("User type already exists", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void testUpdateThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        UserTypeRequest request = new UserTypeRequest("ADMIN");

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.update(1L, request));
        assertEquals("Error updating type", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testDeleteSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenReturn(1);

        Optional<UserTypeEntity> result = repository.delete(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testDeleteNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenReturn(0);

        Optional<UserTypeEntity> result = repository.delete(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteThrowsDataIntegrityViolation() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update())
                .thenThrow(new DataIntegrityViolationException("linked"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.delete(1L));
        assertEquals("User type is being used", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void testDeleteThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        UserTypeRepositoryImpl repository = new UserTypeRepositoryImpl(jdbcClient);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update())
                .thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.delete(1L));
        assertEquals("Error deleting user type", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }
}