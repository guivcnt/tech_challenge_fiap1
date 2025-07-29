package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.entity.MenuEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.service.MenuService;
import br.com.dinewise.domain.requests.menu.MenuRequest;
import br.com.dinewise.domain.responses.DineWiseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dinewise/menu")
@AllArgsConstructor
public class MenuController {
    private final MenuService service;

    @PostMapping
    public ResponseEntity<DineWiseResponse> create(@RequestBody MenuRequest request) throws DineWiseResponseError {
        return this.service.create(request);
    }

    @GetMapping
    public ResponseEntity<List<MenuEntity>> getAll(){
        return this.service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<MenuEntity>> get(@PathVariable Long id) throws DineWiseResponseError {
        return this.service.get(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DineWiseResponse> update(@PathVariable Long id, @RequestBody MenuRequest request) throws DineWiseResponseError {
        return this.service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DineWiseResponse> delete(@PathVariable Long id) throws DineWiseResponseError {
        return this.service.delete(id);
    }
}
