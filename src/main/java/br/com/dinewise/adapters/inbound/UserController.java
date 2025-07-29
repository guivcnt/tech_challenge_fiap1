package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.UserService;
import br.com.dinewise.domain.requests.user.*;
import br.com.dinewise.domain.responses.DineWiseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dinewise/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<DineWiseResponse> createUser(@RequestBody UserRequest user) throws DineWiseResponseError {
        return this.userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<DineWiseResponse> login(@RequestBody LoginRequest request) throws DineWiseResponseError {
        return this.userService.login(request);
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return this.userService.getAll();
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<Optional<UserEntity>> getUser(@PathVariable Long userId) throws DineWiseResponseError {
        return this.userService.get(userId);
    }

    @GetMapping("/login/{userLogin}")
    public ResponseEntity<Optional<UserEntity>> getUser(@PathVariable String userLogin) throws DineWiseResponseError {
        return this.userService.get(userLogin);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<DineWiseResponse> updateUser(@PathVariable Long userId, @RequestBody UserRequest user) throws DineWiseResponseError {
        return this.userService.updateUser(userId, user);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<DineWiseResponse> updatePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest passwordRequest) throws DineWiseResponseError {
        return this.userService.updatePassword(userId, passwordRequest);
    }

    @PutMapping("/{userId}/type")
    public ResponseEntity<DineWiseResponse> updateUserType(@PathVariable Long userId, @RequestBody UserTypeRequest request) throws DineWiseResponseError {
        return this.userService.updateUserType(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<DineWiseResponse> deleteUser(@PathVariable Long userId) throws DineWiseResponseError {
        return this.userService.deleteUser(userId);
    }
}
