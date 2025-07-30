package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.AddressEntity;
import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.AddressRepository;
import br.com.dinewise.domain.requests.address.AddressRequest;
import br.com.dinewise.domain.requests.user.ChangePasswordRequest;
import br.com.dinewise.domain.requests.user.LoginRequest;
import br.com.dinewise.domain.requests.user.UserRequest;
import lombok.SneakyThrows;
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

class UserRepositoryImplTest {

    @Test
    void testCreateUserSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        UserRequest request = new UserRequest("Nome", "email@email.com", "login", "senha", "CLIENT", addressRequest);

        AddressEntity addressEntity = new AddressEntity(5L, "Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000", LocalDateTime.now());
        when(addressRepository.create(addressRequest)).thenReturn(addressEntity);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 42L);
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

        UserEntity entity = repository.createUser(request, 1L);

        assertNotNull(entity);
        assertEquals(42L, entity.getId());
        assertEquals("Nome", entity.getName());
        assertEquals("email@email.com", entity.getEmail());
        assertEquals("login", entity.getLogin());
        assertEquals("senha", entity.getPassword());
        assertEquals("CLIENT", entity.getUserType());
        assertEquals(5L, entity.getAddressId());
        assertNotNull(entity.getLastDateModified());
    }

    @Test
    @SneakyThrows
    void testCreateUserThrowsDuplicateKey() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        UserRequest request = new UserRequest("Nome", "email@email.com", "login", "senha", "CLIENT", addressRequest);

        when(addressRepository.create(addressRequest)).thenReturn(new AddressEntity(1L, null, null, null, null, null, null, null, null));

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update(any(KeyHolder.class)))
                .thenThrow(new DuplicateKeyException("dup"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.createUser(request, 1L));
        assertEquals("User already exists", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    @SneakyThrows
    void testCreateUserThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        UserRequest request = new UserRequest("Nome", "email@email.com", "login", "senha", "CLIENT", addressRequest);

        when(addressRepository.create(addressRequest)).thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.createUser(request, 1L));
        assertEquals("Error creating user", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testLoginSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        LoginRequest request = new LoginRequest("login", "senha");
        UserEntity entity = new UserEntity(1L, "Nome", "email", "login", "senha", "CLIENT", 2L, LocalDateTime.now());

        when(jdbcClient.sql(anyString()).param(anyString(), any()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<UserEntity> result = repository.login(request);

        assertTrue(result.isPresent());
        assertEquals("login", result.get().getLogin());
    }

    @Test
    void testLoginThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        LoginRequest request = new LoginRequest("login", "senha");

        when(jdbcClient.sql(anyString()).param(anyString(), any()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.login(request));
        assertEquals("Error logging in", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testGetAllSuccess() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        List<UserEntity> list = List.of(new UserEntity(1L, "Nome", "email", "login", "senha", "CLIENT", 2L, LocalDateTime.now()));
        when(jdbcClient.sql(anyString()).query(UserEntity.class).list()).thenReturn(list);

        List<UserEntity> result = repository.getAll();
        assertEquals(1, result.size());
        assertEquals("Nome", result.get(0).getName());
    }

    @Test
    void testGetByIdSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        UserEntity entity = new UserEntity(1L, "Nome", "email", "login", "senha", "CLIENT", 2L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<UserEntity> result = repository.get(1L);
        assertTrue(result.isPresent());
        assertEquals("Nome", result.get().getName());
    }

    @Test
    void testGetByIdThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.get(1L));
        assertEquals("Error get user", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testGetByLoginSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        UserEntity entity = new UserEntity(1L, "Nome", "email", "login", "senha", "CLIENT", 2L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<UserEntity> result = repository.get("login");
        assertTrue(result.isPresent());
        assertEquals("login", result.get().getLogin());
    }

    @Test
    void testGetByLoginThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.get("login"));
        assertEquals("Error get user", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testUpdateUserSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        UserRequest request = new UserRequest("Nome", "email@email.com", "login", "senha", "CLIENT", addressRequest);

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

        UserEntity entity = new UserEntity(1L, "Nome", "email@email.com", "login", "senha", "CLIENT", 2L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<UserEntity> result = repository.updateUser(1L, request, 2L);
        assertTrue(result.isPresent());
        assertEquals("Nome", result.get().getName());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testUpdateUserNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        UserRequest request = new UserRequest("Nome", "email@email.com", "login", "senha", "CLIENT", addressRequest);

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

        Optional<UserEntity> result = repository.updateUser(1L, request, 2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUserThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        AddressRequest addressRequest = new AddressRequest("Rua", "10", "Apto", "Bairro", "Cidade", "Estado", "00000-000");
        UserRequest request = new UserRequest("Nome", "email@email.com", "login", "senha", "CLIENT", addressRequest);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.updateUser(1L, request, 2L));
        assertEquals("Error updating user", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testUpdatePasswordSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        ChangePasswordRequest request = new ChangePasswordRequest("old", "new");
        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(1);

        UserEntity entity = new UserEntity(1L, "Nome", "email", "login", "new", "CLIENT", 2L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<UserEntity> result = repository.updatePassword(1L, request);
        assertTrue(result.isPresent());
        assertEquals("new", result.get().getPassword());
    }

    @Test
    void testUpdatePasswordNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        ChangePasswordRequest request = new ChangePasswordRequest("old", "new");
        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(0);

        Optional<UserEntity> result = repository.updatePassword(1L, request);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdatePasswordThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        ChangePasswordRequest request = new ChangePasswordRequest("old", "new");
        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.updatePassword(1L, request));
        assertEquals("Error updating user", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testUpdateUserTypeSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(1);

        UserEntity entity = new UserEntity(1L, "Nome", "email", "login", "senha", "ADMIN", 2L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        Optional<UserEntity> result = repository.updateUserType(1L, 2L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testUpdateUserTypeNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenReturn(0);

        Optional<UserEntity> result = repository.updateUserType(1L, 2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateUserTypeThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString())
                .param(anyString(), any())
                .param(anyString(), any())
                .update()).thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.updateUserType(1L, 2L));
        assertEquals("Error updating user", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void testDeleteUserSuccess() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        UserEntity entity = new UserEntity(1L, "Nome", "email", "login", "senha", "CLIENT", 2L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenReturn(1);

        when(addressRepository.delete(2L)).thenReturn(Optional.of(mock(AddressEntity.class)));

        Optional<UserEntity> result = repository.deleteUser(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testDeleteUserNoRowsChanged() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        UserEntity entity = new UserEntity(1L, "Nome", "email", "login", "senha", "CLIENT", 2L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update()).thenReturn(0);

        Optional<UserEntity> result = repository.deleteUser(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteUserNotFound() throws DineWiseResponseError {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.empty());

        Optional<UserEntity> result = repository.deleteUser(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteUserThrowsDataIntegrityViolation() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        UserEntity entity = new UserEntity(1L, "Nome", "email", "login", "senha", "CLIENT", 2L, LocalDateTime.now());
        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenReturn(Optional.of(entity));

        when(jdbcClient.sql(anyString()).param(anyString(), any()).update())
                .thenThrow(new DataIntegrityViolationException("linked"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.deleteUser(1L));
        assertEquals("User is linked to a restaurant", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void testDeleteUserThrowsException() {
        JdbcClient jdbcClient = mock(JdbcClient.class, RETURNS_DEEP_STUBS);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UserRepositoryImpl repository = new UserRepositoryImpl(jdbcClient, addressRepository);

        when(jdbcClient.sql(anyString()).param(anyString(), any()).query(UserEntity.class).optional())
                .thenThrow(new RuntimeException("fail"));

        DineWiseResponseError ex = assertThrows(DineWiseResponseError.class, () -> repository.deleteUser(1L));
        assertEquals("Error deleting user", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }
}