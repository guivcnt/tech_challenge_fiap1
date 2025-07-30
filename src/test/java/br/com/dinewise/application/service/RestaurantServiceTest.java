package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.RestaurantRepository;
import br.com.dinewise.application.repository.UserRepository;
import br.com.dinewise.domain.requests.address.AddressRequest;
import br.com.dinewise.domain.requests.restaurant.RestaurantRequest;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository repository;
    @Mock
    private UserRepository repositoryUser;

    @InjectMocks
    private RestaurantService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RestaurantRequest buildRequest() {
        return new RestaurantRequest(
                "Restaurante Exemplo",
                "Italiana",
                "aeaeaea",
                "11:00 - 13:00",
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

    @Test
    void testCreateSuccess() throws DineWiseResponseError {
        RestaurantRequest request = buildRequest();
        UserEntity user = new UserEntity();
        user.setId(1L);
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(10L);

        when(repositoryUser.get(request.userLoginOwner())).thenReturn(Optional.of(user));
        when(repository.create(request, user.getId())).thenReturn(restaurant);

        ResponseEntity<DineWiseResponse> response = service.create(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Success creating restaurant! ID: 10"));
        assertEquals(HttpStatus.CREATED, response.getBody().status());
    }

    @Test
    void testCreateUserNotFound() throws DineWiseResponseError {
        RestaurantRequest request = buildRequest();

        when(repositoryUser.get(request.userLoginOwner())).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = service.create(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testCreateRestaurantNotCreated() throws DineWiseResponseError {
        RestaurantRequest request = buildRequest();
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(repositoryUser.get(request.userLoginOwner())).thenReturn(Optional.of(user));
        when(repository.create(request, user.getId())).thenReturn(null);

        ResponseEntity<DineWiseResponse> response = service.create(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restaurant not created", response.getBody().message());
        assertEquals(HttpStatus.CONFLICT, response.getBody().status());
    }

    @Test
    void testGetAll() {
        List<RestaurantEntity> list = List.of(new RestaurantEntity(), new RestaurantEntity());
        when(repository.getAll()).thenReturn(list);

        ResponseEntity<List<RestaurantEntity>> response = service.getAll();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void testGet() throws DineWiseResponseError {
        RestaurantEntity restaurant = new RestaurantEntity();
        when(repository.get(1L)).thenReturn(Optional.of(restaurant));

        ResponseEntity<Optional<RestaurantEntity>> response = service.get(1L);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void testUpdateSuccess() throws DineWiseResponseError {
        RestaurantRequest request = buildRequest();
        RestaurantEntity restaurant = new RestaurantEntity();
        UserEntity newUser = new UserEntity();
        newUser.setId(2L);

        when(repository.get(1L)).thenReturn(Optional.of(restaurant));
        when(repositoryUser.get(request.userLoginOwner())).thenReturn(Optional.of(newUser));

        ResponseEntity<DineWiseResponse> response = service.update(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully updated restaurant 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
        verify(repository).update(1L, request, newUser.getId());
    }

    @Test
    void testUpdateRestaurantNotFound() throws DineWiseResponseError {
        RestaurantRequest request = buildRequest();

        when(repository.get(1L)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = service.update(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restaurant not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testUpdateNewUserNotFound() throws DineWiseResponseError {
        RestaurantRequest request = buildRequest();
        RestaurantEntity restaurant = new RestaurantEntity();

        when(repository.get(1L)).thenReturn(Optional.of(restaurant));
        when(repositoryUser.get(request.userLoginOwner())).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = service.update(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New user not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testDeleteSuccess() throws DineWiseResponseError {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(1L);

        when(repository.delete(1L)).thenReturn(Optional.of(restaurant));

        ResponseEntity<DineWiseResponse> response = service.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully deleted restaurant 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testDeleteRestaurantNotFound() throws DineWiseResponseError {
        when(repository.delete(1L)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = service.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restaurant not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }
}