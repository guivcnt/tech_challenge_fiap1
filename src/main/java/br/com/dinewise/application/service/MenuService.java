package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.MenuEntity;
import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.MenuRepository;
import br.com.dinewise.application.repository.RestaurantRepository;
import br.com.dinewise.application.repository.UserRepository;
import br.com.dinewise.domain.requests.menu.MenuRequest;
import br.com.dinewise.domain.requests.restaurant.RestaurantRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository repository;
    private final RestaurantRepository repositoryRestaurant;

    public ResponseEntity<DineWiseResponse> create(MenuRequest request) throws DineWiseResponseError {
        var entity = repositoryRestaurant.get(request.restaurantId());

        if (entity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("Restaurant not found", HttpStatus.NOT_FOUND));
        }

        var dbResponse = repository.create(request);

        if (dbResponse == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DineWiseResponse("Menu item not created", HttpStatus.CONFLICT));
        }

        return ResponseEntity.accepted().body(new DineWiseResponse("Success creating menu item! ID: " + dbResponse.getId(), HttpStatus.CREATED));
    }

    public ResponseEntity<List<MenuEntity>> getAll() {
        var dbResponse = repository.getAll();
        return ResponseEntity.accepted().body(dbResponse);
    }

    public ResponseEntity<Optional<MenuEntity>> get(Long id) throws DineWiseResponseError {
        return ResponseEntity.accepted().body(repository.get(id));
    }

    public ResponseEntity<DineWiseResponse> update(Long id, MenuRequest request) throws DineWiseResponseError {
        var entityMenu = repository.get(id);

        if (entityMenu.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("Menu item not found", HttpStatus.NOT_FOUND));
        }

        var entity = repositoryRestaurant.get(request.restaurantId());

        if (entity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("Restaurant not found", HttpStatus.NOT_FOUND));
        }

        repository.update(id, request);

        return ResponseEntity.ok(new DineWiseResponse("Successfully updated menu item " + id, HttpStatus.OK));
    }

    public ResponseEntity<DineWiseResponse> delete(Long id) throws DineWiseResponseError {
        var dbResponse = repository.delete(id);

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("Menu item not found", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully deleted menu item " + id, HttpStatus.OK));
    }
}
