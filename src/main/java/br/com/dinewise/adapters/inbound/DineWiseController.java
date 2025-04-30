package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.DineWiseService;
import br.com.dinewise.domain.requests.LoginRequest;
import br.com.dinewise.domain.requests.UserRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dinewise")
public class DineWiseController {

    private final DineWiseService service;

    public DineWiseController(DineWiseService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<DineWiseResponse> login(@RequestBody LoginRequest request) throws DineWiseResponseError {
        return this.service.login(request);
    }


    @PostMapping("/user")
    public ResponseEntity<DineWiseResponse> createUser(@RequestBody UserRequest user) throws DineWiseResponseError {
        return this.service.createUser(user);
    }
    //atualizar usuario
    @PutMapping("/user/{userId}")
    public ResponseEntity<DineWiseResponse> updateUser(@PathVariable Long userId, @RequestBody UserRequest user) throws DineWiseResponseError {
        return this.service.updateUser(userId, user);
    }
    //deletar usuario
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<DineWiseResponse> deleteUser(@PathVariable Long userId) throws DineWiseResponseError {
        return this.service.deleteUser(userId);

    }
}
