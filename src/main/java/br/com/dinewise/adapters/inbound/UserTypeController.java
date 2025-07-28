package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.UserTypeService;
import br.com.dinewise.domain.requests.user.UserTypeRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dinewise/userType")
@AllArgsConstructor
public class UserTypeController {
    private final UserTypeService userTypeService;

    @PostMapping
    public ResponseEntity<DineWiseResponse> create(@RequestBody UserTypeRequest request) throws DineWiseResponseError {
        return this.userTypeService.create(request);
    }

    @GetMapping
    public ResponseEntity<List<UserTypeEntity>> list() throws DineWiseResponseError {
        return this.userTypeService.readAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DineWiseResponse> update(@PathVariable Long id, @RequestBody UserTypeRequest request) throws DineWiseResponseError {
        return this.userTypeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DineWiseResponse> delete(@PathVariable Long id) throws DineWiseResponseError {
        return this.userTypeService.delete(id);
    }
}
