package br.com.dinewise.adapters.inbound;

import br.com.dinewise.application.service.DineWiseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dinewise")
public class DineWiseController {

    private final DineWiseService service;

    public DineWiseController(DineWiseService service) {
        this.service = service;
    }

    //validacao de login
    @GetMapping("/login")
    public void login() {

    }
    //criar usuario
    @PostMapping("/user")
    public void createUser() {

    }
    //atualizar usuario
    @PutMapping("/user/{userId}")
    public void updateUser() {

    }
    //deletar usuario
    @DeleteMapping("/user/{userId}")
    public void deleteUser() {

    }
}
