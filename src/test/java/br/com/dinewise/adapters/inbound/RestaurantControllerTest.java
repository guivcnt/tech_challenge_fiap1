package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.RestaurantService;
import br.com.dinewise.domain.requests.address.AddressRequest;
import br.com.dinewise.domain.requests.restaurant.RestaurantRequest;
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

class RestaurantControllerTest {

    @Mock
    private RestaurantService service;

    @InjectMocks
    private RestaurantController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RestaurantRequest buildRestaurantRequest() {
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

    private RestaurantEntity buildRestaurantEntity() {
        return new RestaurantEntity(
                1L,
                "Restaurante Exemplo",
                "Italiana",
                "08:00 - 22:00",
                1L,
                2L,
                null
        );
    }

    @Test
    void create_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        RestaurantRequest request = buildRestaurantRequest();
        DineWiseResponse response = new DineWiseResponse(null, null);
        when(service.create(request)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<DineWiseResponse> result = controller.create(request);

        verify(service).create(request);
    }

    @Test
    void getAll_deveChamarServiceERetornarLista() {
        List<RestaurantEntity> lista = List.of(buildRestaurantEntity());
        when(service.getAll()).thenReturn(ResponseEntity.ok(lista));

        ResponseEntity<List<RestaurantEntity>> result = controller.getAll();

        assertEquals(lista, result.getBody());
        verify(service).getAll();
    }

    @Test
    void get_deveChamarServiceERetornarOptional() throws DineWiseResponseError {
        Long id = 1L;
        Optional<RestaurantEntity> opt = Optional.of(buildRestaurantEntity());
        when(service.get(id)).thenReturn(ResponseEntity.accepted().body((opt)));

        ResponseEntity<Optional<RestaurantEntity>> result = controller.get(id);

        assertEquals(opt, result.getBody());
        verify(service).get(id);
    }

    @Test
    void update_deveChamarServiceERetornarResponse() throws DineWiseResponseError {
        Long id = 1L;
        RestaurantRequest request = buildRestaurantRequest();
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