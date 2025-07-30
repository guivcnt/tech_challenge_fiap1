package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.entity.MenuEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.MenuService;
import br.com.dinewise.domain.requests.menu.MenuRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MenuControllerTest {

    @Mock
    private MenuService service;

    @InjectMocks
    private MenuController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private MenuRequest buildMenuRequest() {
        return new MenuRequest(
                "Menu 1",
                "Descrição do menu",
                29.99,
                true,
                "/imagens/menu1.png",
                100L
        );
    }

    private MenuEntity buildMenuEntity() {
        return new MenuEntity(
                1L,
                "Menu 1",
                "Descrição do menu",
                29.99,
                true,
                "/imagens/menu1.png",
                100L,
                null
        );
    }

    @Test
    void create_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        MenuRequest request = buildMenuRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(service.create(request)).thenReturn(ResponseEntity.ok(response));

        controller.create(request);

        verify(service).create(request);
    }

    @Test
    void getAll_deveChamarServiceERetornarLista() {
        List<MenuEntity> lista = List.of(buildMenuEntity());
        when(service.getAll()).thenReturn(ResponseEntity.ok(lista));

        ResponseEntity<List<MenuEntity>> result = controller.getAll();

        assertEquals(lista, result.getBody());
        verify(service).getAll();
    }

    @Test
    void get_deveChamarServiceERetornarOptional() throws DineWiseResponseError {
        Long id = 1L;
        Optional<MenuEntity> opt = Optional.of(buildMenuEntity());
        when(service.get(id)).thenReturn(ResponseEntity.accepted().body((opt)));

        ResponseEntity<Optional<MenuEntity>> result = controller.get(id);

        assertEquals(opt, result.getBody());
        verify(service).get(id);
    }

    @Test
    void update_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        MenuRequest request = buildMenuRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(service.update(id, request)).thenReturn(ResponseEntity.ok(response));

        controller.update(id, request);

        verify(service).update(id, request);
    }

    @Test
    void delete_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(service.delete(id)).thenReturn(ResponseEntity.ok(response));

        controller.delete(id);

        verify(service).delete(id);
    }
}