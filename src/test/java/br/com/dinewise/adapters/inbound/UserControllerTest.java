package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.UserService;
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
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

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

    private LoginRequest buildLoginRequest() {
        return new LoginRequest("usuario1", "senha123");
    }

    private ChangePasswordRequest buildChangePasswordRequest() {
        return new ChangePasswordRequest("senhaAntiga", "senhaNova");
    }

    private UserTypeRequest buildUserTypeRequest() {
        return new UserTypeRequest("ADMIN");
    }

    private UserEntity buildUserEntity() {
        return new UserEntity(
                1L,
                "usuario1",
                "usuario1@email.com",
                "CLIENT",
                "senha123",
                "customer",
                1L,
                null
        );
    }

    @Test
    void createUser_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        UserRequest request = buildUserRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userService.createUser(request)).thenReturn(ResponseEntity.ok(response));

        controller.createUser(request);

        verify(userService).createUser(request);
    }

    @Test
    void login_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        LoginRequest request = buildLoginRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userService.login(request)).thenReturn(ResponseEntity.ok(response));

        controller.login(request);

        verify(userService).login(request);
    }

    @Test
    void getAllUsers_deveChamarServiceERetornarLista() {
        List<UserEntity> lista = List.of(buildUserEntity());
        when(userService.getAll()).thenReturn(ResponseEntity.ok(lista));

        controller.getAllUsers();

        verify(userService).getAll();
    }

    @Test
    void getUserPorId_deveChamarServiceERetornarOptional() throws DineWiseResponseError {
        Long id = 1L;
        Optional<UserEntity> opt = Optional.of(buildUserEntity());
        when(userService.get(id)).thenReturn(ResponseEntity.accepted().body((opt)));

        controller.getUser(id);

        verify(userService).get(id);
    }

    @Test
    void getUserPorLogin_deveChamarServiceERetornarOptional() throws DineWiseResponseError {
        String login = "usuario1";
        Optional<UserEntity> opt = Optional.of(buildUserEntity());
        when(userService.get(login)).thenReturn(ResponseEntity.accepted().body((opt)));

        controller.getUser(login);

        verify(userService).get(login);
    }

    @Test
    void updateUser_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        UserRequest request = buildUserRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userService.updateUser(id, request)).thenReturn(ResponseEntity.ok(response));

        controller.updateUser(id, request);

        verify(userService).updateUser(id, request);
    }

    @Test
    void updatePassword_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        ChangePasswordRequest request = buildChangePasswordRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userService.updatePassword(id, request)).thenReturn(ResponseEntity.ok(response));

        controller.updatePassword(id, request);

        verify(userService).updatePassword(id, request);
    }

    @Test
    void updateUserType_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        UserTypeRequest request = buildUserTypeRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userService.updateUserType(id, request)).thenReturn(ResponseEntity.ok(response));

        controller.updateUserType(id, request);

        verify(userService).updateUserType(id, request);
    }

    @Test
    void deleteUser_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userService.deleteUser(id)).thenReturn(ResponseEntity.ok(response));

        controller.deleteUser(id);

        verify(userService).deleteUser(id);
    }
}