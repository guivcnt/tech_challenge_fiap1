package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.UserRepository;
import br.com.dinewise.domain.requests.LoginRequest;
import br.com.dinewise.domain.requests.UserRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DineWiseService {

    private final UserRepository userRepository;

    public DineWiseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<DineWiseResponse> login(LoginRequest request) throws DineWiseResponseError {

        Optional<UserEntity> dbResponse = userRepository.login(request);
// TODO: manter o padrao de resposta...
        if (dbResponse.isEmpty() || dbResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new DineWiseResponse("Success!", HttpStatus.OK));

    }

    public ResponseEntity<DineWiseResponse> createUser(UserRequest user) throws DineWiseResponseError {

        UserEntity dbResponse = userRepository.createUser(user);
// TODO: manter o padrao de resposta...
        if (dbResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.accepted().body(new DineWiseResponse("Success creating user!", HttpStatus.CREATED));
    }

    public ResponseEntity<DineWiseResponse> updateUser(Long userId, UserRequest user) throws DineWiseResponseError {

        Optional<UserEntity> dbResponse = userRepository.updateUser(userId, user);
// TODO: manter o padrao de resposta...
        if (dbResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully updated user " + userId, HttpStatus.OK));
    }

    public ResponseEntity<DineWiseResponse> deleteUser(Long userId) throws DineWiseResponseError {

        Optional<UserEntity> dbResponse = userRepository.deleteUser(userId);
// TODO: manter o padrao de resposta...
        if (dbResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully deleted user " + userId, HttpStatus.OK));
    }
}
