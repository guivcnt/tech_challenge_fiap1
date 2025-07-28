package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.UserService;
import br.com.dinewise.domain.requests.user.ChangePasswordRequest;
import br.com.dinewise.domain.requests.user.ChangeUserTypeRequest;
import br.com.dinewise.domain.requests.user.LoginRequest;
import br.com.dinewise.domain.requests.user.UserRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dinewise")
public class DineWiseController {

    private final UserService service;

    public DineWiseController(UserService service) {
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

    @PutMapping("/user/{userId}")
    public ResponseEntity<DineWiseResponse> updateUser(@PathVariable Long userId, @RequestBody UserRequest user) throws DineWiseResponseError {
        return this.service.updateUser(userId, user);
    }

    @PutMapping("/user/{userId}/password")
    public ResponseEntity<DineWiseResponse> updatePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest passwordRequest) throws DineWiseResponseError {
        return this.service.updatePassword(userId, passwordRequest);
    }

    @PutMapping("/user/{userId}/type")
    public ResponseEntity<DineWiseResponse> updateType(@PathVariable Long userId, @RequestBody ChangeUserTypeRequest request) throws DineWiseResponseError {
        return this.service.updateType(userId, request);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<DineWiseResponse> deleteUser(@PathVariable Long userId) throws DineWiseResponseError {
        return this.service.deleteUser(userId);
    }
}
