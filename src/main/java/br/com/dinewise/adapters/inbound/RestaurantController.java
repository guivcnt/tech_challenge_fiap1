package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.RestaurantService;
import br.com.dinewise.domain.requests.user.LoginRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dinewise/restaurant")
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    @PostMapping("/")
    public ResponseEntity<DineWiseResponse> createRestaurant(@RequestBody LoginRequest request) throws DineWiseResponseError {
        return null;
    }

}
