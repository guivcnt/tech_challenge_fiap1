package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.UserTypeRepository;
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
import static org.mockito.Mockito.when;

class UserTypeServiceTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserTypeService userTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserTypeRequest buildRequest() {
        return new UserTypeRequest("ADMIN");
    }

    @Test
    void testCreateSuccess() throws DineWiseResponseError {
        UserTypeRequest request = buildRequest();
        UserTypeEntity entity = new UserTypeEntity();
        entity.setId(10L);

        when(userTypeRepository.create(request)).thenReturn(entity);

        ResponseEntity<DineWiseResponse> response = userTypeService.create(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Success creating type for user! ID: 10"));
        assertEquals(HttpStatus.CREATED, response.getBody().status());
    }

    @Test
    void testCreateConflict() throws DineWiseResponseError {
        UserTypeRequest request = buildRequest();

        when(userTypeRepository.create(request)).thenReturn(null);

        ResponseEntity<DineWiseResponse> response = userTypeService.create(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Type for user not created", response.getBody().message());
        assertEquals(HttpStatus.CONFLICT, response.getBody().status());
    }

    @Test
    void testReadAll() {
        List<UserTypeEntity> list = List.of(new UserTypeEntity(), new UserTypeEntity());
        when(userTypeRepository.readAll()).thenReturn(list);

        ResponseEntity<List<UserTypeEntity>> response = userTypeService.readAll();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void testUpdateSuccess() throws DineWiseResponseError {
        UserTypeRequest request = buildRequest();
        UserTypeEntity entity = new UserTypeEntity();

        when(userTypeRepository.update(1L, request)).thenReturn(Optional.of(entity));

        ResponseEntity<DineWiseResponse> response = userTypeService.update(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully updated type 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testUpdateNotFound() throws DineWiseResponseError {
        UserTypeRequest request = buildRequest();

        when(userTypeRepository.update(1L, request)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userTypeService.update(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Type for user not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testDeleteSuccess() throws DineWiseResponseError {
        UserTypeEntity entity = new UserTypeEntity();

        when(userTypeRepository.delete(1L)).thenReturn(Optional.of(entity));

        ResponseEntity<DineWiseResponse> response = userTypeService.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully deleted user type 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testDeleteNotFound() throws DineWiseResponseError {
        when(userTypeRepository.delete(1L)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = userTypeService.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User type not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }
}