package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.UserRepository;
import br.com.dinewise.application.repository.UserTypeRepository;
import br.com.dinewise.domain.requests.address.AddressRequest;
import br.com.dinewise.domain.requests.user.ChangePasswordRequest;
import br.com.dinewise.domain.requests.user.LoginRequest;
import br.com.dinewise.domain.requests.user.UserRequest;
import br.com.dinewise.domain.requests.user.UserTypeRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserRequest buildUserRequest() {
        return new UserRequest(
                "usuario1",
                "usuario1@email.com",
                "usuario",
                "senha123",
                "customer",
                new AddressRequest(
                        "Rua Exemplo",
                        "123",
                        "Apto 1",
                        "Bairro Exemplo",
                        "Cidade Exemplo",
                        "SP",
                        "01234-567"
                )
        );
    }

    private UserTypeRequest buildUserTypeRequest() {
        return new UserTypeRequest("ADMIN");
    }

    private LoginRequest buildLoginRequest() {
        return new LoginRequest("user1", "pass");
    }

    private ChangePasswordRequest buildChangePasswordRequest() {
        return new ChangePasswordRequest("pass", "newpass");
    }

    @Test
    void testCreateUserSuccess() throws DineWiseResponseError {
        UserRequest request = buildUserRequest();
        UserTypeEntity userType = new UserTypeEntity();
        userType.setId(2L);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(10L);

        when(userTypeRepository.read(any(UserTypeRequest.class))).thenReturn(Optional.of(userType));
        when(userRepository.createUser(request, userType.getId())).thenReturn(userEntity);

        ResponseEntity<DineWiseResponse> response = userService.createUser(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Success creating user! ID: 10"));
        assertEquals(HttpStatus.CREATED, response.getBody().status());
    }

    @Test
    void testCreateUserTypeNotFound() throws DineWiseResponseError {
        UserRequest request = buildUserRequest();

        when(userTypeRepository.read(any(UserTypeRequest.class))).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userService.createUser(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User type not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testCreateUserNotCreated() throws DineWiseResponseError {
        UserRequest request = buildUserRequest();
        UserTypeEntity userType = new UserTypeEntity();
        userType.setId(2L);

        when(userTypeRepository.read(any(UserTypeRequest.class))).thenReturn(Optional.of(userType));
        when(userRepository.createUser(request, userType.getId())).thenReturn(null);

        ResponseEntity<DineWiseResponse> response = userService.createUser(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not created", response.getBody().message());
        assertEquals(HttpStatus.CONFLICT, response.getBody().status());
    }

    @Test
    void testLoginSuccess() throws DineWiseResponseError {
        LoginRequest request = buildLoginRequest();
        UserEntity userEntity = new UserEntity();

        when(userRepository.login(request)).thenReturn(Optional.of(userEntity));

        ResponseEntity<DineWiseResponse> response = userService.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success!", response.getBody().message());
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testLoginFail() throws DineWiseResponseError {
        LoginRequest request = buildLoginRequest();

        when(userRepository.login(request)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userService.login(request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found or password incorrect.", response.getBody().message());
        assertEquals(HttpStatus.FORBIDDEN, response.getBody().status());
    }

    @Test
    void testGetAll() {
        List<UserEntity> users = List.of(new UserEntity(), new UserEntity());
        when(userRepository.getAll()).thenReturn(users);

        ResponseEntity<List<UserEntity>> response = userService.getAll();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetById() throws DineWiseResponseError {
        UserEntity user = new UserEntity();
        when(userRepository.get(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Optional<UserEntity>> response = userService.get(1L);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void testGetByLogin() throws DineWiseResponseError {
        UserEntity user = new UserEntity();
        when(userRepository.get("user1")).thenReturn(Optional.of(user));

        ResponseEntity<Optional<UserEntity>> response = userService.get("user1");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void testUpdateUserSuccess() throws DineWiseResponseError {
        UserRequest request = buildUserRequest();
        UserTypeEntity userType = new UserTypeEntity();
        userType.setId(2L);
        UserEntity user = new UserEntity();

        when(userTypeRepository.read(any(UserTypeRequest.class))).thenReturn(Optional.of(userType));
        when(userRepository.updateUser(1L, request, userType.getId())).thenReturn(Optional.of(user));

        ResponseEntity<DineWiseResponse> response = userService.updateUser(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully updated user 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testUpdateUserNotFound() throws DineWiseResponseError {
        UserRequest request = buildUserRequest();
        UserTypeEntity userType = new UserTypeEntity();
        userType.setId(2L);

        when(userTypeRepository.read(any(UserTypeRequest.class))).thenReturn(Optional.of(userType));
        when(userRepository.updateUser(1L, request, userType.getId())).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userService.updateUser(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testUpdatePasswordSuccess() throws DineWiseResponseError {
        ChangePasswordRequest request = buildChangePasswordRequest();
        UserEntity user = new UserEntity();

        when(userRepository.updatePassword(1L, request)).thenReturn(Optional.of(user));

        ResponseEntity<DineWiseResponse> response = userService.updatePassword(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully updated user 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testUpdatePasswordFail() throws DineWiseResponseError {
        ChangePasswordRequest request = buildChangePasswordRequest();

        when(userRepository.updatePassword(1L, request)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userService.updatePassword(1L, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found or password incorrect.", response.getBody().message());
        assertEquals(HttpStatus.FORBIDDEN, response.getBody().status());
    }

    @Test
    void testUpdateUserTypeSuccess() throws DineWiseResponseError {
        UserTypeRequest request = buildUserTypeRequest();
        UserTypeEntity userType = new UserTypeEntity();
        userType.setId(2L);
        UserEntity user = new UserEntity();

        when(userTypeRepository.read(any(UserTypeRequest.class))).thenReturn(Optional.of(userType));
        when(userRepository.updateUserType(1L, userType.getId())).thenReturn(Optional.of(user));

        ResponseEntity<DineWiseResponse> response = userService.updateUserType(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully updated user 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testUpdateUserTypeNotFound() throws DineWiseResponseError {
        UserTypeRequest request = buildUserTypeRequest();

        when(userTypeRepository.read(any(UserTypeRequest.class))).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userService.updateUserType(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User type not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testUpdateUserTypeUserNotFound() throws DineWiseResponseError {
        UserTypeRequest request = buildUserTypeRequest();
        UserTypeEntity userType = new UserTypeEntity();
        userType.setId(2L);

        when(userTypeRepository.read(any(UserTypeRequest.class))).thenReturn(Optional.of(userType));
        when(userRepository.updateUserType(1L, userType.getId())).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userService.updateUserType(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testDeleteUserSuccess() throws DineWiseResponseError {
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.deleteUser(1L)).thenReturn(Optional.of(user));

        ResponseEntity<DineWiseResponse> response = userService.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully deleted user 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testDeleteUserNotFound() throws DineWiseResponseError {
        when(userRepository.deleteUser(1L)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userService.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }
}