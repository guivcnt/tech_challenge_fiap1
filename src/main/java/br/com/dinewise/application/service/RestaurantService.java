package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.RestaurantRepository;
import br.com.dinewise.application.repository.UserRepository;
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
public class RestaurantService {
    private final RestaurantRepository repository;
    private final UserRepository repositoryUser;

    public ResponseEntity<DineWiseResponse> create(RestaurantRequest request) throws DineWiseResponseError {
        var entity = repositoryUser.get(request.userLoginOwner());

        if (entity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("User not found", HttpStatus.NOT_FOUND));
        }

        var dbResponse = repository.create(request, entity.get().getId());

        if (dbResponse == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DineWiseResponse("Restaurant not created", HttpStatus.CONFLICT));
        }

        return ResponseEntity.accepted().body(new DineWiseResponse("Success creating restaurant! ID: " + dbResponse.getId(), HttpStatus.CREATED));
    }

    public ResponseEntity<List<RestaurantEntity>> getAll() {
        var dbResponse = repository.getAll();
        return ResponseEntity.accepted().body(dbResponse);
    }

    public ResponseEntity<Optional<RestaurantEntity>> get(Long id) throws DineWiseResponseError {
        return ResponseEntity.accepted().body(repository.get(id));
    }

    public ResponseEntity<DineWiseResponse> update(Long id, RestaurantRequest request) throws DineWiseResponseError {
        var entityRestaurant = repository.get(id);

        if (entityRestaurant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("Restaurant not found", HttpStatus.NOT_FOUND));
        }

        var entityNewUser = repositoryUser.get(request.userLoginOwner());

        if (entityNewUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("New user not found", HttpStatus.NOT_FOUND));
        }

        repository.update(id, request, entityNewUser.get().getId());

        return ResponseEntity.ok(new DineWiseResponse("Successfully updated restaurant " + id, HttpStatus.OK));
    }

    public ResponseEntity<DineWiseResponse> delete(Long id) throws DineWiseResponseError {
        var dbResponse = repository.delete(id);

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("Restaurant not found", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully deleted restaurant " + id, HttpStatus.OK));
    }

}
