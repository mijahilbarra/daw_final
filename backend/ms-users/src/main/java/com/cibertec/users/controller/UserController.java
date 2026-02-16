package com.cibertec.users.controller;

import com.cibertec.users.dto.LoginRequest;
import com.cibertec.users.entity.UserEntity;
import com.cibertec.users.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserEntity> findAll(@RequestParam Map<String, String> filters) {
        return service.findAll(filters);
    }

    @PostMapping
    public UserEntity create(@RequestBody UserEntity user) {
        return service.create(user);
    }

    @PatchMapping("/{id}")
    public UserEntity update(@PathVariable String id, @RequestBody Map<String, Object> patch) {
        return service.update(id, patch);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/login")
    public UserEntity login(@RequestBody LoginRequest request) {
        return service.login(request);
    }
}
