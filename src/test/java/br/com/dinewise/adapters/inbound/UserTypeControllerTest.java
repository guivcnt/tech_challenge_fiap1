package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.UserTypeService;
import br.com.dinewise.domain.requests.user.UserTypeRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserTypeControllerTest {

    @Mock
    private UserTypeService userTypeService;

    @InjectMocks
    private UserTypeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserTypeRequest buildUserTypeRequest() {
        return new UserTypeRequest("ADMIN");
    }

    private UserTypeEntity buildUserTypeEntity() {
        return new UserTypeEntity(1L, "ADMIN", null);
    }

    @Test
    void create_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        UserTypeRequest request = buildUserTypeRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userTypeService.create(request)).thenReturn(ResponseEntity.ok(response));

        controller.create(request);

        verify(userTypeService).create(request);
    }

    @Test
    void list_deveChamarServiceERetornarLista() throws DineWiseResponseError {
        List<UserTypeEntity> lista = List.of(buildUserTypeEntity());
        when(userTypeService.readAll()).thenReturn(ResponseEntity.ok(lista));

        controller.list();

        verify(userTypeService).readAll();
    }

    @Test
    void update_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        UserTypeRequest request = buildUserTypeRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userTypeService.update(id, request)).thenReturn(ResponseEntity.ok(response));

        controller.update(id, request);

        verify(userTypeService).update(id, request);
    }

    @Test
    void delete_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(userTypeService.delete(id)).thenReturn(ResponseEntity.ok(response));

        controller.delete(id);

        verify(userTypeService).delete(id);
    }
}