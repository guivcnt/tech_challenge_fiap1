package br.com.dinewise.application.service;

import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.UserTypeRepository;
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
public class UserTypeService {
    private final UserTypeRepository userTypeRepository;

    public ResponseEntity<DineWiseResponse> create(UserTypeRequest type) throws DineWiseResponseError {
        UserTypeEntity dbResponse = userTypeRepository.create(type);

        if (dbResponse == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DineWiseResponse("Type for user not created", HttpStatus.CONFLICT));
        }

        return ResponseEntity.accepted().body(new DineWiseResponse("Success creating type for user! ID: " + dbResponse.getId(), HttpStatus.CREATED));
    }

    public ResponseEntity<List<UserTypeEntity>> readAll() {
        List<UserTypeEntity> dbResponse = userTypeRepository.readAll();
        return ResponseEntity.accepted().body(dbResponse);
    }

    public ResponseEntity<DineWiseResponse> update(Long id, UserTypeRequest request) throws DineWiseResponseError {
        Optional<UserTypeEntity> dbResponse = userTypeRepository.update(id, request);

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("Type for user not found", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully updated type " + id, HttpStatus.OK));
    }

    public ResponseEntity<DineWiseResponse> delete(Long id) throws DineWiseResponseError {
        Optional<UserTypeEntity> dbResponse = userTypeRepository.delete(id);

        if (dbResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DineWiseResponse("User type not found", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok(new DineWiseResponse("Successfully deleted user type " + id, HttpStatus.OK));
    }
}
