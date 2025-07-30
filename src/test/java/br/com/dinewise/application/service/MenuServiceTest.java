package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.MenuEntity;
import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.MenuRepository;
import br.com.dinewise.application.repository.RestaurantRepository;
import br.com.dinewise.domain.requests.menu.MenuRequest;
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

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private MenuRequest buildMenuRequest() {
        return new MenuRequest(
                "Test Menu Item",
                "Delicious test item",
                9.99,
                true,
                "test-image-path.jpg",
                1L
        );
    }

    @Test
    void testCreateSuccess() throws DineWiseResponseError {
        MenuRequest request = buildMenuRequest();
        RestaurantEntity restaurant = new RestaurantEntity();
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setId(10L);

        when(restaurantRepository.get(request.restaurantId())).thenReturn(Optional.of(restaurant));
        when(menuRepository.create(request)).thenReturn(menuEntity);

        ResponseEntity<DineWiseResponse> response = menuService.create(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Success creating menu item! ID: 10"));
        assertEquals(HttpStatus.CREATED, response.getBody().status());
    }

    @Test
    void testCreateRestaurantNotFound() throws DineWiseResponseError {
        MenuRequest request = buildMenuRequest();

        when(restaurantRepository.get(request.restaurantId())).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = menuService.create(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restaurant not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testCreateMenuNotCreated() throws DineWiseResponseError {
        MenuRequest request = buildMenuRequest();
        RestaurantEntity restaurant = new RestaurantEntity();

        when(restaurantRepository.get(request.restaurantId())).thenReturn(Optional.of(restaurant));
        when(menuRepository.create(request)).thenReturn(null);

        ResponseEntity<DineWiseResponse> response = menuService.create(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Menu item not created", response.getBody().message());
        assertEquals(HttpStatus.CONFLICT, response.getBody().status());
    }

    @Test
    void testGetAll() {
        List<MenuEntity> menuList = List.of(new MenuEntity(), new MenuEntity());
        when(menuRepository.getAll()).thenReturn(menuList);

        ResponseEntity<List<MenuEntity>> response = menuService.getAll();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(menuList, response.getBody());
    }

    @Test
    void testGet() throws DineWiseResponseError {
        MenuEntity menu = new MenuEntity();
        when(menuRepository.get(1L)).thenReturn(Optional.of(menu));

        ResponseEntity<Optional<MenuEntity>> response = menuService.get(1L);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void testUpdateSuccess() throws DineWiseResponseError {
        MenuRequest request = buildMenuRequest();
        MenuEntity menu = new MenuEntity();
        RestaurantEntity restaurant = new RestaurantEntity();

        when(menuRepository.get(1L)).thenReturn(Optional.of(menu));
        when(restaurantRepository.get(request.restaurantId())).thenReturn(Optional.of(restaurant));

        ResponseEntity<DineWiseResponse> response = menuService.update(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully updated menu item 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
        verify(menuRepository).update(1L, request);
    }

    @Test
    void testUpdateMenuNotFound() throws DineWiseResponseError {
        MenuRequest request = buildMenuRequest();

        when(menuRepository.get(1L)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = menuService.update(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Menu item not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testUpdateRestaurantNotFound() throws DineWiseResponseError {
        MenuRequest request = buildMenuRequest();
        MenuEntity menu = new MenuEntity();

        when(menuRepository.get(1L)).thenReturn(Optional.of(menu));
        when(restaurantRepository.get(request.restaurantId())).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = menuService.update(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Restaurant not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }

    @Test
    void testDeleteSuccess() throws DineWiseResponseError {
        MenuEntity menu = new MenuEntity();
        menu.setId(1L);

        when(menuRepository.delete(1L)).thenReturn(Optional.of(menu));

        ResponseEntity<DineWiseResponse> response = menuService.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Successfully deleted menu item 1"));
        assertEquals(HttpStatus.OK, response.getBody().status());
    }

    @Test
    void testDeleteMenuNotFound() throws DineWiseResponseError {
        when(menuRepository.delete(1L)).thenReturn(Optional.empty());

        ResponseEntity<DineWiseResponse> response = menuService.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Menu item not found", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().status());
    }
}