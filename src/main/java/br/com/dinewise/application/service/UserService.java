package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.UserRepository;
import br.com.dinewise.application.repository.UserTypeRepository;
import br.com.dinewise.domain.requests.user.ChangePasswordRequest;
import br.com.dinewise.domain.requests.user.LoginRequest;
import br.com.dinewise.domain.requests.user.UserRequest;
import br.com.dinewise.domain.requests.user.UserTypeRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;

    public ResponseEntity<DineWiseResponse> createUser(UserRequest user) throws DineWiseResponseError {
        Optional<UserTypeEntity> idUserType = userTypeRepository.read(new UserTypeRequest(user.userType()));

        if (idUserType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("User type not found", HttpStatus.NOT_FOUND));
        }

        UserEntity dbResponse = userRepository.createUser(user, idUserType.get().getId());

        if (dbResponse == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DineWiseResponse("User not created", HttpStatus.CONFLICT));
        }

        return ResponseEntity.accepted().body(new DineWiseResponse("Success creating user! ID: " + dbResponse.getId(), HttpStatus.CREATED));
    }

    public ResponseEntity<DineWiseResponse> login(LoginRequest request) throws DineWiseResponseError {

        Optional<UserEntity> dbResponse = userRepository.login(request);

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DineWiseResponse("User not found or password incorrect.", HttpStatus.FORBIDDEN));
        }

        return ResponseEntity.ok(new DineWiseResponse("Success!", HttpStatus.OK));

    }

    public ResponseEntity<List<UserEntity>> getAll() {
        return ResponseEntity.accepted().body(userRepository.getAll());
    }

    public ResponseEntity<Optional<UserEntity>> get(Long userId) throws DineWiseResponseError {
        return ResponseEntity.accepted().body(userRepository.get(userId));
    }

    public ResponseEntity<Optional<UserEntity>> get(String userLogin) throws DineWiseResponseError {
        return ResponseEntity.accepted().body(userRepository.get(userLogin));
    }

    public ResponseEntity<DineWiseResponse> updateUser(Long userId, UserRequest user) throws DineWiseResponseError {
        Optional<UserTypeEntity> idUserType = userTypeRepository.read(new UserTypeRequest(user.userType()));

        if (idUserType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("User type not found", HttpStatus.NOT_FOUND));
        }

        Optional<UserEntity> dbResponse = userRepository.updateUser(userId, user, idUserType.get().getId());

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("User not found", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully updated user " + userId, HttpStatus.OK));
    }

    public ResponseEntity<DineWiseResponse> updatePassword(Long userId, ChangePasswordRequest passwordRequest) throws DineWiseResponseError {

        Optional<UserEntity> dbResponse = userRepository.updatePassword(userId, passwordRequest);

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DineWiseResponse("User not found or password incorrect.", HttpStatus.FORBIDDEN));
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully updated user " + userId, HttpStatus.OK));
    }

    public ResponseEntity<DineWiseResponse> updateUserType(Long userId, UserTypeRequest type) throws DineWiseResponseError {
        Optional<UserTypeEntity> idUserType = userTypeRepository.read(new UserTypeRequest(type.userType()));

        if (idUserType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("User type not found", HttpStatus.NOT_FOUND));
        }

        Optional<UserEntity> dbResponse = userRepository.updateUserType(userId, idUserType.get().getId());

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("User not found", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully updated user " + userId, HttpStatus.OK));
    }

    public ResponseEntity<DineWiseResponse> deleteUser(Long userId) throws DineWiseResponseError {

        Optional<UserEntity> dbResponse = userRepository.deleteUser(userId);

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("User not found", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully deleted user " + userId, HttpStatus.OK));
    }
}
