package com.cibertec.users.service;

import com.cibertec.users.dto.LoginRequest;
import com.cibertec.users.entity.UserEntity;
import com.cibertec.users.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserEntity> findAll(Map<String, String> filters) {
        List<UserEntity> rows = repository.findAll();

        if (filters == null || filters.isEmpty()) {
            return rows;
        }

        return rows.stream()
                .filter(row -> matchesFilters(row, filters))
                .toList();
    }

    public UserEntity create(UserEntity user) {
        if (user.getUserRole() == null || user.getUserRole().isBlank()) {
            user.setUserRole("transportista");
        }
        return repository.save(user);
    }

    public UserEntity update(String id, Map<String, Object> patch) {
        UserEntity current = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (patch.containsKey("userName")) {
            current.setUserName(String.valueOf(patch.get("userName")));
        }
        if (patch.containsKey("userEmail")) {
            current.setUserEmail(String.valueOf(patch.get("userEmail")));
        }
        if (patch.containsKey("userPassword")) {
            current.setUserPassword(String.valueOf(patch.get("userPassword")));
        }
        if (patch.containsKey("userRole")) {
            current.setUserRole(String.valueOf(patch.get("userRole")));
        }

        return repository.save(current);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public UserEntity login(LoginRequest request) {
        return repository.findByUserEmail(request.email())
                .filter(user -> user.getUserPassword().equals(request.password()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));
    }

    private boolean matchesFilters(UserEntity row, Map<String, String> filters) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            Object fieldValue = readField(row, key);
            if (fieldValue == null) {
                return false;
            }

            if (!String.valueOf(fieldValue).equals(value)) {
                return false;
            }
        }

        return true;
    }

    private Object readField(UserEntity row, String fieldName) {
        try {
            Field field = UserEntity.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(row);
        } catch (Exception e) {
            return null;
        }
    }
}
