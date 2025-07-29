package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.RestaurantService;
import br.com.dinewise.domain.requests.restaurant.RestaurantRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dinewise/restaurant")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantService service;

    @PostMapping
    public ResponseEntity<DineWiseResponse> createRestaurant(@RequestBody RestaurantRequest request) throws DineWiseResponseError {
        return this.service.create(request);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantEntity>> getAll(){
        return this.service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<RestaurantEntity>> getRestaurant(@PathVariable Long id) throws DineWiseResponseError {
        return this.service.get(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DineWiseResponse> updateUser(@PathVariable Long id, @RequestBody RestaurantRequest request) throws DineWiseResponseError {
        return this.service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DineWiseResponse> updateUser(@PathVariable Long id) throws DineWiseResponseError {
        return this.service.delete(id);
    }
}
